package tech.picnic.assignment.impl;

import tech.picnic.assignment.api.StreamProcessor;
import tech.picnic.assignment.model.Article;
import tech.picnic.assignment.model.Pick;
import tech.picnic.assignment.model.Picker;

import java.io.*;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            // Pick pick = Pick.fromJson(line); ? (make each object have code responsible for marshalling its properties)
        }

        Stream<Pick> picksStream = picks.stream()
                .filter(pick -> pick.getArticle().getTemperatureZone().equals("ambient"));
        picksStream.forEach(pick -> pick.getArticle().makeNameUpperCase());
        Map<Picker, List<Pick>> groupedPicksByPicker = picksStream.collect(Collectors.groupingBy(Pick::getPicker));
        groupedPicksByPicker.forEach((id, picksByPicker) -> picksByPicker.sort(Comparator.comparing(Pick::getTimestamp)));
        // Marshal/Serialize the groupedPicksByPicker to JSON, according to the given standard
        // Or introduce our own class to represent the output. And to be filled with the data from the groupedPicksByPicker


        close();
    }

    @Override
    public void close() {
        StreamProcessor.super.close();
    }
}
