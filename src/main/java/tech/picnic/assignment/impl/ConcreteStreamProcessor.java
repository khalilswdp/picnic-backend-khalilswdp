package tech.picnic.assignment.impl;

import tech.picnic.assignment.api.StreamProcessor;
import tech.picnic.assignment.model.ArticleProcessedInput;
import tech.picnic.assignment.model.Pick;
import tech.picnic.assignment.model.Picker;
import tech.picnic.assignment.model.PickerProcessedInput;

import java.io.*;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static tech.picnic.assignment.model.PickerProcessedInput.getPickerProcessedInputs;
import static tech.picnic.assignment.model.PickerProcessedInput.toJson;

public class ConcreteStreamProcessor implements StreamProcessor {

    int maxEvents;

    public ConcreteStreamProcessor(int maxEvents, Duration maxTime) {
        this.maxEvents = maxEvents;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                close();
            }
        }, maxTime.toMillis());

    }

    @Override
    public void process(InputStream source, OutputStream sink) throws IOException {
        // The input is formatted as NDJsons.
        BufferedReader reader = new BufferedReader(new InputStreamReader(source));

        List<Pick> picks = new ArrayList<>();
        while(reader.ready() && maxEvents != 0) {
            maxEvents--;
            String line = reader.readLine();
            if (line.equals("\n")) {
                continue;
            }
            // Unmarshal/Deserialize the JSON. and create a Pick object. and add it to the picks list
            try {
                Pick pick = Pick.fromJson(line);
                picks.add(pick);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        reader.close();

        List<PickerProcessedInput> pickerProcessedInputs = getPickerProcessedInputs(picks);

        toJson(sink, pickerProcessedInputs);

        close();
    }

    @Override
    public void close() {
        StreamProcessor.super.close();
    }
}
