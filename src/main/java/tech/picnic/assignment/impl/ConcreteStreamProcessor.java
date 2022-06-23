package tech.picnic.assignment.impl;

import tech.picnic.assignment.api.StreamProcessor;
import tech.picnic.assignment.model.Pick;
import tech.picnic.assignment.model.PickerProcessedInput;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static tech.picnic.assignment.model.PickerProcessedInput.getPickerProcessedInputs;
import static tech.picnic.assignment.model.PickerProcessedInput.toJson;

public class ConcreteStreamProcessor implements StreamProcessor {

    int maxEvents;
    Duration maxTime;

    public ConcreteStreamProcessor(int maxEvents, Duration maxTime) {
        this.maxEvents = maxEvents;
        this.maxTime = maxTime;
    }

    @Override
    public void process(InputStream source, OutputStream sink) throws IOException {
        // The input is formatted as NDJsons.
        BufferedReader reader = new BufferedReader(new InputStreamReader(source));

        List<Pick> picks = new ArrayList<>();

        Instant start = Instant.now();
        int eventCountLeft = this.maxEvents;

        while(eventCountLeft > 0 && Duration.between(start, Instant.now()).compareTo(this.maxTime) <= 0 && reader.ready()) {
            eventCountLeft--;
            String line = reader.readLine();
            if (line.equals("\n")) {
                continue;
            }
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
    }

    @Override
    public void close() {
        StreamProcessor.super.close();
    }
}
