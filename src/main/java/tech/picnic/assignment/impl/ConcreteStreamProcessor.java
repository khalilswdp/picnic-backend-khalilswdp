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


        // Filter and Make Article Name Upper Case
        Stream<Pick> picksStream = picks.stream()
                .filter(pick -> pick.getArticle().getTemperature_zone().equals("ambient"))
                .peek(pick -> pick.getArticle().makeNameUpperCase());

        // Group by Picker
        Map<Picker, List<Pick>> groupedPicksByPicker = picksStream.collect(Collectors.groupingBy(Pick::getPicker));

        // Sort Picks by Timestamp
        groupedPicksByPicker.forEach((id, picksByPicker) -> picksByPicker.sort(Comparator.comparing(Pick::getTimestamp)));

        // The Pickers must be sorted chronologically (ascending) by their active_since timestamp, breaking ties by ID.
        List<Picker> sortedPickers = groupedPicksByPicker.keySet().stream()
                .sorted(Comparator.comparing(Picker::getActive_since).thenComparing(Picker::getId))
                .collect(Collectors.toList());

        // Map the previous into a list of processedinput
        List<PickerProcessedInput> pickerProcessedInputs = new ArrayList<>();
        for (Picker picker : sortedPickers) {
            List<Pick> picksByPicker = groupedPicksByPicker.get(picker);
            pickerProcessedInputs.add(
                    new PickerProcessedInput(
                            picker.getName(),
                            picker.getActive_since(),
                            picksByPicker.stream().map(
                                    pick -> new ArticleProcessedInput(
                                            pick.getArticle().getName(),
                                            pick.getTimestamp())).collect(Collectors.toList())));
        }
        
        
        // Marshal/Serialize the groupedPicksByPicker to JSON, and write it to the sink, according to the given standard
        // Or introduce our own class to represent the output. And to be filled with the data from the groupedPicksByPicker
        // and the sortedPickers.
        toJson(sink, pickerProcessedInputs);

        close();
    }

    @Override
    public void close() {
        StreamProcessor.super.close();
    }
}
