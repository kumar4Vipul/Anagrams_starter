/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();

    private int wordength = DEFAULT_WORD_LENGTH;

    public ArrayList<String > wordList= new ArrayList<>();
    public HashMap<String ,ArrayList<String >> lettertowords = new HashMap<>();
    public HashSet<String > wordset= new HashSet<>();

    public HashMap<Integer,ArrayList<String>> sizetoword = new HashMap<>();


    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordset.add(word);

            String sortedWord = sortLetters(word);

            if (lettertowords.containsKey(sortedWord)){
                lettertowords.get(sortedWord).add(word);

            }
            else {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(word);
                lettertowords.put(sortedWord,temp);
            }
            if (sizetoword.containsKey(word.length())){
                sizetoword.get(word.length()).add(word);
            }
            else {
                ArrayList<String> temp =new ArrayList<>();
                temp.add(word);
                sizetoword.put(word.length(),temp);

            }
        }


    }


    public boolean isGoodWord(String word, String base) {
        return wordset.contains(word)&&!word.contains(base);
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sortedTargetWord = sortLetters(targetWord);

        //first step is to iterate through all 10000 words and find the anagrams
        for (String word : wordList) {
            //sort the word
            String sortedWord = sortLetters(word);

            //if it matches to sortedTargetWord, then it's an anagram of it
            if (sortedTargetWord.equals(sortedWord)) {
                //add the original word
                result.add(word);
            }
        }

        return result;
    }



    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();

        String alphabets = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < alphabets.length(); i++) {

            String tempString = word;

            //add every letter and get the key
            tempString += alphabets.charAt(i);

            String key = sortLetters(tempString);

            //check if that key exists
            if (lettertowords.containsKey(key)) {

                //get all the values for that key
                ArrayList<String> tempList = lettertowords.get(key);

                //check if the obtained words are notGoodWords again
                ArrayList<String> removeList = new ArrayList<>();
                for (String test : tempList) {
                    if (!isGoodWord(test, word)) {
                        removeList.add(test);
                    }
                }

                //remove all the notGoodWords
                result.removeAll(removeList);

                //add the list to the remaining list to be returned
                result.addAll(tempList);
            }
        }

        return result;
    }

    public String pickGoodStarterWord() {
        while (true) {

            //get all words with 3/4/5 letters and pick from them only
            ArrayList<String> tempList = sizetoword.get(wordength);

            //generate a random number between 0 and sizeOf list obtained
            Random random = new Random();
            int num = random.nextInt(tempList.size());


            //pick random word from the arrayList
//            String randomWord = wordList.get(num);
            String randomWord = tempList.get(num);

            //get all the anagrams for that random word
            ArrayList<String> arrayList= (ArrayList<String>) getAnagramsWithOneMoreLetter(randomWord);

            //validate the conditions given
            if ((randomWord.length() == wordength) && arrayList.size() > MIN_NUM_ANAGRAMS) {

                //increment the wordLength for next stage
                if (wordength < MAX_WORD_LENGTH) wordength++;
                return randomWord;
            }
        }

    }

    public String sortLetters(String word){
        char[] words= word.toCharArray();
        Arrays.sort(words);
        return new String(words);
    }
}
