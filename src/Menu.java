
import java.io.*;
import java.util.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import static javafx.application.Platform.exit;

public class Menu extends Application {
    Contestant[] contestants = new Contestant[20];

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Scanner sc = new Scanner(System.in);

        String option = "z";

        while (!option.equalsIgnoreCase("Q")) { //select the options
            System.out.println("\n<<------Welcome To British Indoor Rowing Championships !!!------>>");
            System.out.println("Enter \"G\" to Get File Data And Store :");
            System.out.println("Enter \"O\" to View Overall Winner And Show Their Details :");
            System.out.println("Enter \"F\" to View Fasted Winners And Show Their Details   :");
            System.out.println("Enter \"S\" to Store Winner' Details to File :");
            System.out.println("Enter \"Q\" Quite The Programme :");

            option = sc.next();

            switch (option) {
                case "G":
                case "g":
                    try {
                        System.out.println("Get File Data and Store :");
                        getFileDataAndStore(contestants);


                    } catch (Exception e) {
                        System.out.println("Invalid Input");
                    }
                    break;
                case "O":
                case "o":
                    try {
                        System.out.println("Overall Winner And Show Their Details  :");
                        Contestant overallWinner = findOverallWinner();
                        System.out.println("Winner's Name         : " + overallWinner.getName());
                        System.out.println("Winner's Club         : " + overallWinner.getClub());
                        System.out.println("Winner's Age          : " + overallWinner.getAge());
                        System.out.println("Winner's Winning Time : " + getStringFormatFromMilliSeconds(overallWinner.getSegment2000Time()));
                        viewOverallWinner(overallWinner);
                    } catch (Exception e) {
                        System.out.println("Invalid Input");
                    }
                    break;
                case "F":
                case "f":
                    try {

                        System.out.println("View Fasted Winners And Show Their Details : ");
                        Contestant segmentWinner = findFastedSegmentWinner();
                        String[] segmentAndTime = fastestSegmentAndTime(segmentWinner);

                        System.out.println("Fastest Segment Winner's Name : " + segmentWinner.getName());
                        System.out.println("Fastest Segment Winner's Club : " + segmentWinner.getClub());
                        System.out.println("Fastest Segment Winner's Age  : " + segmentWinner.getAge());
                        System.out.println("Winning Segment               : " + segmentAndTime[0]);
                        System.out.println("Fastest Winner's Winning Time : " + segmentAndTime[1]);


                        viewFastestWinners(segmentWinner, segmentAndTime);
                    } catch (Exception e) {
                        System.out.println("Invalid Input");
                    }
                    break;

                case "S":
                case "s":
                    try {
                        System.out.println("Store Data To File");
                        Contestant overallWinner = findOverallWinner();
                        Contestant segmentWinner = findFastedSegmentWinner();
                        String[] segmentAndTime = fastestSegmentAndTime(segmentWinner);
                        storeDataToFile(segmentWinner, overallWinner, segmentAndTime);
                    } catch (Exception e) {
                        System.out.println("Invalid Input");
                    }
                    break;


                case "Q":
                case "q":
                    System.out.println("quit the program");
                    exit();
                    break;
            }
        }
    }


    // read from file and store data in obj array
    private void getFileDataAndStore(Contestant[] contestants) {
        try {
            File bricObj = new File("src/birc.txt");
            Scanner textFileReader = new Scanner(bricObj);
            textFileReader.nextLine(); // skip title line(Name,Club,Age,500,1000,1500,2000)
            int i = 0;
            while (textFileReader.hasNextLine()) {
                String[] data = textFileReader.nextLine().split(";"); //split values
                Contestant contestant = new Contestant();
                contestant.setName(data[0].trim()); //trim() for remove the spaces
                contestant.setClub(data[1].trim());
                contestant.setAge(Integer.parseInt(data[2].trim()));
                contestant.setSegment500Time(convertIntoMillisecond(data[3].trim()));
                contestant.setSegment1000Time(convertIntoMillisecond(data[4].trim()));
                contestant.setSegment1500Time(convertIntoMillisecond(data[5].trim()));
                contestant.setSegment2000Time(convertIntoMillisecond(data[6].trim()));

                contestants[i] = contestant;
                //show stored data in CLI
                System.out.println("Competitor Number             : " + (i + 1) + "\nCompetitor Name               : " + contestants[i].getName() + "\nCompetitor Club               : " + contestants[i].getClub() + "\nCompetitor Age                : " + contestants[i].getAge() + "\nCompetitor 500m Segment Time  : " + (data[3].trim()) + "\nCompetitor 1000m Segment Time : " + (data[4].trim()) + "\nCompetitor 1500m Segment Time : " + (data[5].trim()) + "\nCompetitor 2000m Segment Time : " + (data[6].trim()) + "\n");

                i += 1;
            }
            textFileReader.close();


        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }


    }

    // input "01:45.7" output: 105007
    private long convertIntoMillisecond(String timer) {
        String[] parts = timer.split("[:.]");

        int minutes = Integer.parseInt(String.valueOf(parts[0]));
        int sec = Integer.parseInt(String.valueOf(parts[1]));
        int ms = Integer.parseInt(String.valueOf(parts[2]));
        long millisecond = minutes * 60 * 1000 + sec * 1000 + ms;

        return millisecond;
    }

    //find this champion overall winner
    private Contestant findOverallWinner() {
        long minValue = -1;
        Contestant selectedMin = null;
        for (int i = 0; i < contestants.length; i++) {
            if (i == 0) {
                minValue = contestants[i].getSegment2000Time(); // i==0 competitor's 2000 segment value put to minValue
                selectedMin = contestants[i];

                continue;
            }
            //compare minValue with other competitor's 2000 segment value(i=1 to i=19)
            if (contestants[i].getSegment2000Time() < minValue) {
                minValue = contestants[i].getSegment2000Time();
                selectedMin = contestants[i];
            }

        }

        return selectedMin;


    }

    //view overall winner in GUI
    private void viewOverallWinner(Contestant c) {
        Stage stage = new Stage();
        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-padding: 10;");
        vbox.setStyle("-fx-background-color: #330000");
        Scene scene = new Scene(vbox, 600, 350);
        stage.setScene(scene);
        stage.setTitle("British Indoor Rowing Championships ");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // create text are for show tittle of View Overall Winner
        TextArea areaTittle = new TextArea();
        areaTittle.setEditable(false);
        areaTittle.setText("                        View Overall Winner");
        areaTittle.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        areaTittle.setPrefHeight(25);
        areaTittle.setPrefWidth(100);
        gridPane.add(areaTittle, 10, 3);


        // create text are for show overall winner's details in GUI
        TextArea areaDetails = new TextArea();
        areaDetails.setEditable(false);
        areaDetails.setText("\nWinner's Name              : " + c.getName() + "\nWinner's Club                : " + c.getClub() + "\nWinner's Age                 : " + c.getAge() + "\nWinner's Winning Time : " + getStringFormatFromMilliSeconds(c.getSegment2000Time()));
        areaDetails.setPrefHeight(150);
        areaDetails.setPrefWidth(410);
        gridPane.add(areaDetails, 10, 7);


        vbox.getChildren().add(gridPane);
        stage.setScene(scene);
        stage.showAndWait();

    }

    //find minimum time value and contestant then return winner in contestant object
    private Contestant findFastedSegmentWinner() {
        long minValue = -1;
        Contestant selectedMin = null;


        for (int i = 0; i < contestants.length; i++) {
            //create variable for compare segment values
            long valueFor500 = contestants[i].getSegment500Time();
            long valueFor1000 = contestants[i].getSegment1000Time() - valueFor500;
            long valueFor1500 = contestants[i].getSegment1500Time() - valueFor1000;
            long valueFor2000 = contestants[i].getSegment2000Time() - valueFor1500;

            if (i == 0) {

                minValue = valueFor500; //i==0 competitor's valueFor50 put to minValue
            }
            //compare minValue with other competitor's value
            if (valueFor500 < minValue) {
                minValue = valueFor500;
                selectedMin = contestants[i];

            }
            if (valueFor1000 < minValue) {
                minValue = valueFor1000;
                selectedMin = contestants[i];
            }
            if (valueFor1500 < minValue) {
                minValue = valueFor1000;
                selectedMin = contestants[i];

            }
            if (valueFor2000 < minValue) {
                minValue = valueFor2000;
                selectedMin = contestants[i];
            }


        }


        return selectedMin;
    }

    //compare minimum time of four 500 segment sets to find winner's time and their segment
    private String[] fastestSegmentAndTime(Contestant segmentWinner) {
        String segment;
        String minSegmentTime;
        long minTime;

        long valueForSegment1 = segmentWinner.getSegment500Time();
        long valueForSegment2 = segmentWinner.getSegment1000Time() - segmentWinner.getSegment500Time();
        long valueForSegment3 = segmentWinner.getSegment1500Time() - segmentWinner.getSegment1000Time();
        long valueForSegment4 = segmentWinner.getSegment2000Time() - segmentWinner.getSegment1500Time();

        minTime = valueForSegment1; // valueForSegment1 put to minTime
        minSegmentTime = getStringFormatFromMilliSeconds(minTime);  //minTime convert to string
        segment = "500m segment";

        if (valueForSegment2 < minTime) {
            minTime = valueForSegment2;
            minSegmentTime = getStringFormatFromMilliSeconds(minTime);
            segment = "500m to 1000m segment";


        }
        if (valueForSegment3 < minTime) {
            minTime = valueForSegment3;
            minSegmentTime = getStringFormatFromMilliSeconds(minTime);
            segment = "1000m to 1500m segment";


        }
        if (valueForSegment4 < minTime) {
            minTime = valueForSegment4;
            minSegmentTime = getStringFormatFromMilliSeconds(minTime);
            segment = "1500m to 2000m segment";


        }

        String[] segmentAndTime = {segment, minSegmentTime};

        return segmentAndTime;

    }


    //find fasted winners for four segments
    private void viewFastestWinners(Contestant segmentWinner, String[] segmentAndTime) {
        Stage stage = new Stage();
        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-padding: 10;");
        vbox.setStyle("-fx-background-color: #330000");
        Scene scene = new Scene(vbox, 600, 350);
        stage.setScene(scene);
        stage.setTitle("British Indoor Rowing Championships ");


        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // create text are for show tittle of View fastest segment Winner
        TextArea areaTittle = new TextArea();
        areaTittle.setEditable(false);
        areaTittle.setText("                        View Fastest Winner");
        areaTittle.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        areaTittle.setPrefHeight(25);
        areaTittle.setPrefWidth(100);
        gridPane.add(areaTittle, 10, 3);


        // create text are for show fastest segment winner's details in GUI
        TextArea areaDetails = new TextArea();
        areaDetails.setEditable(false);
        areaDetails.setText("\nWinner's Name               : " + segmentWinner.getName() + "\nWinner's Club                 : " + segmentWinner.getClub() + "\nWinner's Age                  : " + segmentWinner.getAge() + "\nWinning Segment           : " + segmentAndTime[0] + "\nWinner's Winning Time  : " + segmentAndTime[1]);
        areaDetails.setPrefHeight(150);
        areaDetails.setPrefWidth(410);
        gridPane.add(areaDetails, 10, 7);


        vbox.getChildren().add(gridPane);
        stage.setScene(scene);
        stage.showAndWait();

    }

    //millisecond convert to string(input:8888  output:"01:30.02")
    private String getStringFormatFromMilliSeconds(long time) {

        String timeInFormat = (time / 60 / 1000) + ":" + ((time / 1000) % 60) + "." + (time % 1000);

        return timeInFormat;
    }


    //store data to file
    private void storeDataToFile(Contestant segmentWinner, Contestant c, String[] segmentAndTime) {

        //write to file : "data"

        try {
            File file = new File("src/winner.txt");
            FileOutputStream fos = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(fos);

            pw.println("Overall Winner Details :--->");
            pw.println(c.getName() + "," + c.getClub() + "," + c.getAge() + "," + getStringFormatFromMilliSeconds(c.getSegment2000Time()));

            pw.println("\nFastest Segment Winner's Details :--->");
            pw.println(segmentWinner.getName() + "," + segmentWinner.getClub() + "," + segmentWinner.getAge() + "," + segmentAndTime[0] + "," + segmentAndTime[1]);

            pw.flush(); //to the stream
            pw.close();
            fos.close(); //closes this file output stream
        } catch (Exception e) {
            System.out.println("Error in file writing");
        }

    }

}




