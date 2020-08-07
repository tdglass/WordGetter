package com.timothy;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.*;


public class Controller {
    @FXML Button getWordsButton;
    @FXML TextField htmlTextField;
    @FXML TextArea htmlWordArea;
    @FXML TextField addWordField;
    @FXML TextArea removeWordListArea;
    @FXML Button addWordButton;
    @FXML Button analyzeButton;
    @FXML TextArea analyzedTextArea;
    String wordsOfWebsite;
    String[] removeWords;
    List<String> wordsList;

    public void getWords() throws IOException {
        htmlWordArea.clear();


        String website = htmlTextField.getText();   //Get user entered URL.
        Elements doc = Jsoup.connect(website).get().select("p");
        /*
        Jsoup Elements -> https://jsoup.org/apidocs/org/jsoup/select/Elements.html
        Jsoup connect("http://example.com").get() -> https://jsoup.org/apidocs/org/jsoup/Jsoup.html#connect(java.lang.String)
        Jsoup CSS selector -> https://jsoup.org/apidocs/org/jsoup/nodes/Element.html#cssSelector()
         */


        /*  Jsoup "doc" type -> https://jsoup.org/apidocs/org/jsoup/nodes/Document.html
        Store Jsoup document type as a string, using a setter.
        Using regular expressions, replace everything besides words and single spaces.
        */
        setWordsOfWebsite(String.valueOf(doc).trim().replaceAll("\\[[^\\]]+\\]", " ").replaceAll("<[^>]+>"," ").replaceAll("([^a-zA-Z0-9\\s])", "").replaceAll("\\s{2,}", " "));

        /*
        Put the long string of words into an arraylist.
        Use the split method to separate every word with a space " ".
        */
        setWordsList(wordsList = new ArrayList<>(Arrays.asList(getWordsOfWebsite().toLowerCase().split(" "))));




        /*
        Display results, with at least 75 characters per line
        and maintaining complete words.
         */
        int lineLength = 0;
        for (int i = 0; i < getWordsList().size(); i++) {
            if (lineLength >= 75){
                htmlWordArea.appendText("\n");
                lineLength = 0;
            }
            htmlWordArea.appendText(getWordsList().get(i) + " ");
            lineLength += getWordsList().get(i).length();
        }

    }





    public void addWordButton (){       //Add words that you do not want in the calculation.
        removeWordListArea.appendText(addWordField.getText() + "\n");
        addWordField.clear();
    }


    public void analyzeButton() {
        //Split method comes back as an array and is passed to setRemoveWords method.
        setRemoveWords(removeWordListArea.getText().split("[\n]"));


        //            |Iteration word|       |Array of words|
        for (String wordInRemoveWordsArray : getRemoveWords()) {
            getWordsList().removeAll(Collections.singleton(wordInRemoveWordsArray));
        }   /*
            Use for each loop to iterate over every word in the removeWords array.
            Remove that word if it is contained inside of the large, wordsList ArrayList.
            */





        /*
        Maps aren't typically sorted by value, usually they are sorted by key.
        I would like to sort by value.. how many times the word appears.

        Note -> when calling the .get(key) method it returns the value associated with that key.
         */


        LinkedHashMap<String, Integer> wordMap = new LinkedHashMap<>();
        for (int i = 0; i <= wordsList.size() - 1; i++) {

            if(wordMap.containsKey(wordsList.get(i))){

                //               |Associated key|          |Increase the value by 1|
                wordMap.replace(  wordsList.get(i),    wordMap.get(wordsList.get(i))+1  );

            } else wordMap.put(wordsList.get(i),1);
            //If no Key is found, store that entry in the map and assign its value to 1.

        }



        /*
        Store keys and values into separate lists for sorting.
        Using a linked hashmap should retain order so that each index
        in the lists will correspond with one another keeping the key
        value accurate.
         */

        List<String> sortedWordsList = new ArrayList<>(wordMap.keySet());
        List<Integer> sortedNumbersList = new ArrayList<>(wordMap.values());

        boolean swap;

        do {
            swap = false;
            for (int i = 0; i < wordMap.size() - 2; i++) {
                if (sortedNumbersList.get(i) < sortedNumbersList.get(i + 1)) {  //Keep moving the higher values to the left
                    Collections.swap(sortedNumbersList, i, i + 1);
                    Collections.swap(sortedWordsList, i, i + 1);
                    swap = true;
                }
            }
        } while (swap);                                                        //If no swaps are made, break the look.



        analyzedTextArea.clear();

        for (int i = 0; i <= wordMap.size()-1 ; i++) {
            analyzedTextArea.appendText(sortedWordsList.get(i) + " : " + sortedNumbersList.get(i)  + "\n");
        }


    }


    public String getWordsOfWebsite() {
        return wordsOfWebsite;
    }


    public void setWordsOfWebsite(String refinedWordList) {
        this.wordsOfWebsite = refinedWordList;
    }


    public String[] getRemoveWords() {
        return removeWords;
    }

    public void setRemoveWords(String[] removeWords) {
        this.removeWords = removeWords;
    }

    public List<String> getWordsList() {
        return wordsList;
    }

    public void setWordsList(List<String> wordsList) {
        this.wordsList =  wordsList;
    }
}