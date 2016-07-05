/**
 * Created by GeniAse on 7/4/16.
 */

//Imports are listed in full to show what's being used
//could just import javax.swing.* and java.awt.* etc..
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.*;
import javax.swing.JList;
import java.awt.BorderLayout;
import java.io.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

//import java.awt.event.ActionListener;
//import java.awt.event.ActionEvent;
public class GUI {
    public void GUI() {
        JFrame guiFrame = new JFrame();
        //make sure the program exits when the frame closes
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("Company Information Provider");
        guiFrame.setSize(900, 650);

        //This will center the JFrame in the middle of the screen
        guiFrame.setLocationRelativeTo(null);
        //Options for the JComboBox
        String[] queryoptions = {"Continent","Company", "Account", "Country", "CEO", "DepartmentCategory",
                "Shareholders", "SICCode", "Adress","ComanySIC","Mortgage","previousname","limitedpartership","CompanyRegisteredInEngland",
                "eumembership","CompanyWithContinent"};
        //Options for the JList

        //final  List<String> sparqleQueries = new ArrayList<String>();

        //The first JPanel contains a JLabel and JCombobox
        final JPanel comboPanel = new JPanel();
        JLabel comboLbl = new JLabel("Select what you want to know:");
        JComboBox QueriesComboBox = new JComboBox(queryoptions);
        comboPanel.add(comboLbl);
        comboPanel.add(QueriesComboBox);

        //Create the second JPanel. Add a JLabel and JList and
        //make use the JPanel is not visible.
        final JPanel listPanel = new JPanel();
        listPanel.setVisible(false);
        JLabel listLbl = new JLabel("Result:");

        listPanel.add(listLbl);


        final SearchDatabase SD = new SearchDatabase();
        QueriesComboBox.addActionListener(new ActionListener() {

         //   @Override
            public void actionPerformed(ActionEvent event) {
                listPanel.setVisible(!listPanel.isVisible());
                comboPanel.setVisible(comboPanel.isVisible());
                listPanel.removeAll();
                JComboBox QueriesComboBox = (JComboBox) event.getSource();

                Object selected = QueriesComboBox.getSelectedItem();
                try {
                    String[] sparqleQueries = SD.runQuery(selected.toString());
                    JList QueryList = new JList(sparqleQueries);

                    QueryList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                    listPanel.add(QueryList);
                    //listPanel.add( new JScrollPane( QueryList));
                    System.out.println(selected.toString());
                }
                catch(IOException ie) {
                    ie.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }});


        JPanel container = new JPanel();
        container.add(comboPanel,BorderLayout.NORTH);
        container.add(listPanel,BorderLayout.CENTER);
        JScrollPane jsp = new JScrollPane(container);
        guiFrame.add(jsp);
        //The JFrame uses the BorderLayout layout manager.
        //Put the two JPanels and JButton in different areas.
        //guiFrame.add(comboPanel, BorderLayout.NORTH);
        //guiFrame.add(listPanel, BorderLayout.CENTER);
        //guiFrame.add(generateResult,BorderLayout.SOUTH);
        //make sure the JFrame is visible
        guiFrame.setVisible(true);
    }
}


