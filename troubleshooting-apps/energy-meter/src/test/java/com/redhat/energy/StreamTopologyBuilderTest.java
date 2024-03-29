package com.redhat.energy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.TestRecord;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.redhat.energy.records.WindTurbineStats;
import com.redhat.energy.records.MWattsMeasurement;
import com.redhat.energy.records.WindTurbine;

import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.IntegerDeserializer;

public class StreamTopologyBuilderTest {

    TopologyTestDriver testDriver;
    TestInputTopic<Integer, Integer> wattsStream;
    TestInputTopic<Integer, WindTurbine> turbinesStream;
    TestOutputTopic<Integer, MWattsMeasurement> measurementsStream;
    TestOutputTopic<Integer, WindTurbineStats> statsStream;

    @BeforeEach
    public void setup() {
        // TODO: create the test driver
        StreamTopologyBuilder builder = new StreamTopologyBuilder();
        testDriver = new TopologyTestDriver(builder.buildTopology());

        // TODO: Create test topics
        wattsStream = testDriver.createInputTopic(
                "turbine-generated-watts",
                new IntegerSerializer(),
                new IntegerSerializer());

        ObjectMapperSerde<MWattsMeasurement> measurementSerde = new ObjectMapperSerde<>(MWattsMeasurement.class);
        measurementsStream = testDriver.createOutputTopic("turbine-generated-mwatts",
                new IntegerDeserializer(),
                measurementSerde.deserializer());
    }

    @AfterEach
    public void tearDown() {
        testDriver.close();
    }

    // TODO: implement test
    @Test
    public void testMWattsConversion() {
        wattsStream.pipeInput(1, 2500000);

        TestRecord<Integer, MWattsMeasurement> record = measurementsStream.readRecord();

        assertEquals(2.5, record.getValue().megawatts);
    }
}