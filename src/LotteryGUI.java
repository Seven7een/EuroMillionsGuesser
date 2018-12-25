import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LotteryGUI {

    JFrame mainframe;
    HashMap<Integer, Integer> ballCounts;
    HashMap<Integer, Integer> luckyCounts;

    JLabel[] ballLabels = new JLabel[5];
    JLabel[] luckyLabels = new JLabel[2];

    public LotteryGUI(){
        init();
    }

    private void init() {

        mainframe = new JFrame("Lottery Guesser");
        Container contentPane = mainframe.getContentPane();
        contentPane.setLayout(new BorderLayout());

        JButton getPrediction = new JButton("Get prediction");
        contentPane.add(getPrediction, BorderLayout.NORTH);

        Container results = new Container();
        results.setLayout(new FlowLayout());

        for(int i = 0; i < 5; i++){
            ballLabels[i] = new JLabel();
            ballLabels[i].setOpaque(true);
            ballLabels[i].setBackground(Color.lightGray);
            ballLabels[i].setFont(new Font(Font.SERIF, Font.BOLD, 16));
            results.add(ballLabels[i]);
        }

        results.add(new JLabel("  "));

        for(int i = 0; i < 2; i++){
            luckyLabels[i] = new JLabel();
            luckyLabels[i].setOpaque(true);
            luckyLabels[i].setBackground(Color.orange);
            luckyLabels[i].setFont(new Font(Font.SERIF, Font.BOLD, 16));
            results.add(luckyLabels[i]);
        }

        contentPane.add(results, BorderLayout.CENTER);

        getPrediction.addActionListener(e -> {

            calculateMostCommon();

            try {
                HttpURLConnection.setFollowRedirects(true); // defaults to true

                String url = "https://www.national-lottery.co.uk/results/euromillions/draw-history/csv";
                URL request_url = new URL(url);
                URLConnection conn = request_url.openConnection();

                if (conn != null) {
                    try {

                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String currentLine;
                        br.readLine();

                        while (true) {
                            currentLine = br.readLine();
                            if (currentLine == null) {
                                break;
                            }

                            String[] values = currentLine.split(",");
                            for(int x = 1; x < 6; x++){
                                ballCounts.put(Integer.parseInt(values[x]), ballCounts.get(Integer.parseInt(values[x])) + 1);
                            }

                            for(int y = 6; y < 8; y++){
                                luckyCounts.put(Integer.parseInt(values[y]), luckyCounts.get(Integer.parseInt(values[y])) + 1);
                            }

                        }

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            for(int i = 0; i < 5; i++){
                ballLabels[i].setText(String.valueOf(Collections.max(ballCounts.entrySet(), Map.Entry.comparingByValue()).getKey()));
                ballCounts.put(Collections.max(ballCounts.entrySet(), Map.Entry.comparingByValue()).getKey(), 0);
            }

            for(int i = 0; i < 2; i++){
                luckyLabels[i].setText(String.valueOf(Collections.max(luckyCounts.entrySet(), Map.Entry.comparingByValue()).getKey()));
                luckyCounts.put(Collections.max(luckyCounts.entrySet(), Map.Entry.comparingByValue()).getKey(), 0);
            }
        });

        mainframe.setLocationRelativeTo(null);
        mainframe.setSize(200, 100);
        mainframe.setVisible(true);
        mainframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void calculateMostCommon(){

        ballCounts = new HashMap<>();
        luckyCounts = new HashMap<>();

        for(int i = 1; i < 51; i++){
            ballCounts.put(i, 0);
        }

        for(int j = 1; j < 51; j++){
            luckyCounts.put(j, 0);
        }

    }

}
