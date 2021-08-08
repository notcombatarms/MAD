package edu.curtin.quizflag.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.curtin.quizflag.R;

/**
 * Singleton class that contains all the hard coded question objects the app will use. Also contain
 * some other data, such as winning point and starting point that are used to determine when game
 * end or not.
 *
 * @author      Tao Hu
 * Date         20/09/2020
 */

public class Game
{
    private static Game game = null;
    private int currentPoint;
    private int winningPoint;
    private boolean specialQuestionAnswered; /**this means that a special question was answered*/
    private QuestionSet currentQuestionSet;
    private Map<Integer, QuestionSet> questionSets;

    private Game()
    {
        currentPoint = 0;
        winningPoint = 0;
        currentQuestionSet = null;
        questionSets = new HashMap<>();
        specialQuestionAnswered = false;

        //Questions retrieved from
        QuestionSet qs = new QuestionSet();
        questionSets.put(R.drawable.flag_vn, qs);//Vietnam
        qs.addQuestion(new Question("Vietnam is bounded in the southwest by", new String[]{"Japan", "Bight of Guinea", "Indonesia", "Cambodia"}, "Cambodia", 20, 10));
        qs.addQuestion(new Question("What is the capital city of the country?", new String[]{"Nha Trang", "Hue", "Da Nang", "Hanoi"}, "Hanoi", 15, 5));
        qs.addQuestion(new Question("As of 2018, what percentage of the Vietnamese population practice Christianity?", new String[]{"10.8 percent", "19 percent", "8.3 percent"}, "8.3 percent", 15, 5));
        qs.addQuestion(new Question("When did the country gain independence?", new String[]{"May 13, 1949", "January 1941", "July 9, 1947", "September 2, 1945"}, "September 2, 1945", 20, 10));
        qs.addQuestion(new Question("What percentage of the Vietnamese territory is made up of water?", new String[]{"8.3 percent", "6.4 percent"}, "6.4 percent", 20, 10));
        qs.addQuestion(new Question("When did northern Vietnam cease being a Chinese territory?", new String[]{"AD 781", "AD 939", "AD 949", "AD 780"}, "AD 939", 30, 15, true));


        //Questions retrieved from https://resources.finalsite.net/images/v1551130473/davisk12utus/ioiwulsvlouxnqvzelw0/USGovernmentandCitizenshipTest-MultipleChoiceQuesitonsandAnswers2019.pdf
        qs = new QuestionSet();
        questionSets.put(R.drawable.flag_us, qs);
        qs.addQuestion(new Question(" We elect a President for how many years", new String[]{"Eight", "Two", "Four", "Ten"}, "Four", 20, 10));
        qs.addQuestion(new Question(" How many US senators does Utah have", new String[]{"1", "2", "3", "4"}, "2", 20, 10));
        qs.addQuestion(new Question(" What is one right or freedom granted by the First Amendment", new String[]{"Trial by jury", "To vote", "To bear arms", "Speech"}, "Speech", 20, 10));
        qs.addQuestion(new Question(" What is the title of the highest political official in a US state", new String[]{"Governor", " Lieutenant Governor", " Senate President", "Chief of Police"}, "Governor", 20, 10));
        qs.addQuestion(new Question(" Who makes federal laws", new String[]{"Congress", "The states", "The President", "The Supreme Court"}, "Congress", 20, 10, true));
        qs.addQuestion(new Question(" How many amendments does the Constitution have", new String[]{"19", "18", "25", "27"}, "27", 20, 10));
        qs.addQuestion(new Question(" Who wrote the Declaration of Independence", new String[]{"Abraham Lincoln", "James Madison", "George Washington", "Thomas Jefferson"}, "Thomas Jefferson", 20, 10));
        qs.addQuestion(new Question(" Who signs bills to become laws", new String[]{"The Chief Justice of the Supreme Court", "The Vice President", "The Secretary of State", "The President"}, "The President", 20, 10));
        qs.addQuestion(new Question(" What is the capital of the United States", new String[]{"St", "Olympia, WA", "New York, NY", "Washington, DC"}, "Washington, DC", 20, 10));
        qs.addQuestion(new Question(" Who is the Commander in Chief of the military", new String[]{"The President", "The Vice-President", "The Secretary of Defense", "The Attorney General"}, "The President", 20, 10));

        //Questions retrieved from https://www.express.co.uk/life-style/life/1294716/Multiple-choice-quiz-questions-and-answers
        qs = new QuestionSet();
        questionSets.put(R.drawable.flag_uk, qs);
        qs.addQuestion(new Question("Who is the Patron Saint of Spain?", new String[]{"St James", "St John", "St Benedict"}, "St James", 20, 10));
        qs.addQuestion(new Question("Which of these means a speech in a play where a character talks to themselves rather than to other characters?", new String[]{"Interlude", "Revue", "Soliloquy"}, "Soliloquy", 20, 10));
        qs.addQuestion(new Question("In the Vicar of Dibley, what was the name of the vicar's clueless friend?", new String[]{"Alice", "Beatrice", "Charlotte"}, "Alice", 20, 10));
        qs.addQuestion(new Question("Casterly Rock is the ancestral home of which family in Game of Thrones?", new String[]{"The Starks", "The Tully's", "The Lannisters"}, "The Lannisters", 20, 10));
        qs.addQuestion(new Question("Which breed of dog used to be sacred in China?", new String[]{"Cockapoo", "Pekingese", "Spaniel"}, "Pekingese", 20, 10));
        qs.addQuestion(new Question("Which philosopher coined the term 'I think, therefore I am'?", new String[]{"Plato", "Descartes", "Socrates"}, "Descartes", 20, 10));
        qs.addQuestion(new Question("Who was mayor of London before Boris Johnson?", new String[]{"Sadiq Khan", "John Major", "Ken Livingston"}, "Ken Livingston", 20, 10));
        qs.addQuestion(new Question("Which two countries are Europes biggest exporters of beers?", new String[]{"Germany and Belgium", "Germany and Spain", "Belgium and Spain"}, "Germany and Belgium", 20, 10));
        qs.addQuestion(new Question("Which two calendar months are named after Roman Emperors?", new String[]{"July and June", "December and May", "July and August"}, "July and August", 20, 10));
        qs.addQuestion(new Question("How many novels did the Bronte sisters write in total?", new String[]{"Nine", "Seven", "Eight"}, "Seven", 20, 10));

        qs = new QuestionSet();
        questionSets.put(R.drawable.flag_ru, qs);//Russia
        qs.addQuestion(new Question("Who was the first czar of Russia?", new String[]{"Peter I (the Great)", "Nicholas II", "Ivan III (the Great)", "Ivan IV (the Terrible)"}, "Ivan III (the Great)", 20, 10));
        qs.addQuestion(new Question("Approximately how many Russians were killed during Stalin's reign?", new String[]{"20,000", "2,000", "20,000,000", "200,000"}, "20,000,000", 20, 10));
        qs.addQuestion(new Question("Who was the Soviet premier during the Cuban Missile Crisis?", new String[]{"Stalin", "Yeltsin", "Brezhnev", "Kruschev"}, "Kruschev", 20, 10));
        qs.addQuestion(new Question("What is the term used for emperor (or king) in Russia?", new String[]{"Serf", "Czar", "Coup", "Missionary"}, "Czar", 20, 10));
        qs.addQuestion(new Question("Who was the last czar of Russia?", new String[]{"Peter I", "Catherine II", "Nicholas II", "Ivan III"}, "Nicholas II", 20, 10));

        //From //From https://go4quiz.com/category/australia/
        qs = new QuestionSet();
        questionSets.put(R.drawable.flag_au, qs);
        qs.addQuestion(new Question("When was Anthony William Greig born?", new String[]{"9 February 1944", "8 April 1940", "7 August 1949", "6 October 1946"}, "6 October 1946", 20, 10));
        qs.addQuestion(new Question("Where was Anthony William Greig born?", new String[]{"Durban", "Pretoria", "Queenstown", "Cape Town"}, "Queenstown", 20, 10));
        qs.addQuestion(new Question("Which college did Anthony William Greig attend?", new String[]{"Queen’s College", "St. Paul’s College", "Sacred Heart College", "Government College"}, "Queen’s College", 20, 10));
        qs.addQuestion(new Question("Against which country did Anthony William Greig make his Test debut?", new String[]{"Australia", "India", "New Zealand", "Pakistan"}, "Australia", 20, 10));
        qs.addQuestion(new Question("Who was controversially run out by Anthony William Greig in West Indies in 1974?", new String[]{"Lawrence Rowe", "Alvin Kallicharan", "Rohan Kanhai", "Bernard Julien"}, "Alvin Kallicharan", 20, 10));
        qs.addQuestion(new Question("Where did Anthony William Greig play his first Test as England’s captain?", new String[]{"Lord’s", "Edgbaston", "Old Trafford", "Oval"}, "Lord’s", 20, 10));
        qs.addQuestion(new Question("Whom did Anthony William Greig help to found World Series Cricket?", new String[]{"Rupert Murdoch", "Robert Maxwell", "Kerry Packer", "Edward Turner"}, "Kerry Packer", 20, 10));
        qs.addQuestion(new Question("When did Anthony William Greig die?", new String[]{"26 January 2009", "14 June 2008", "30 September 2002", "29 December 2012"}, "29 December 2012", 20, 10));
        qs.addQuestion(new Question("Where did Anthony William Greig die?", new String[]{"Sydney", "Trinidad", "Christchurch", "Salisbury"}, "Sydney", 20, 10));
        qs.addQuestion(new Question("At which hospital did Anthony William Greig die?", new String[]{"St. Andrew’s Hospital", "Holy Spirit Hospital", "St. Vincent’s Hospital", "St. Margaret’s Hospital"}, "St. Vincent’s Hospital", 20, 10));


        //From https://go4quiz.com/category/china/
        qs = new QuestionSet();
        questionSets.put(R.drawable.flag_cn, qs);
        qs.addQuestion(new Question("Which city was known as Peking?", new String[]{"Beijing", "Shanghai", "Xian", "Guangzhou"}, "Beijing", 20, 10));
        qs.addQuestion(new Question("What is the meaning of Tiananmen?", new String[]{"Desert", "Beautiful River", "Heavenly gate", "Glacier"}, "Heavenly gate", 20, 10));
        qs.addQuestion(new Question("What is the official name of China?", new String[]{"Republic of China", "People’s Republic of China", "Kingdom of China", "Commonwealth of China"}, "People’s Republic of China", 20, 10));
        qs.addQuestion(new Question("When did China get back Hong Kong?", new String[]{"1949", "1964", "1982", "1997"}, "1997", 20, 10));
        qs.addQuestion(new Question("When did China get back Macao?", new String[]{"2001", "1999", "2004", "1984"}, "1999", 20, 10));
        qs.addQuestion(new Question("Which country was annexed by China in 1950?", new String[]{"Tibet", "Nepal", "Mongolia", "Vietnam"}, "Tibet", 20, 10));
        qs.addQuestion(new Question("Which country was attacked by China in 1962?", new String[]{"USSR", "India", "Pakistan", "Ceylon"}, "India", 20, 10));
        qs.addQuestion(new Question("Who ruled China when Marco Polo reached there in 1266?", new String[]{"Kublai Khan", "Chengiz Khan", "Babur", "Akbar"}, "Kublai Khan", 20, 10));
        qs.addQuestion(new Question("Who became the President of China in 1912?", new String[]{"Hua Kuo Feng", "Mao Zedong", "Sun Yat Sen", "Lin Piao"}, "Sun Yat Sen", 20, 10));
        qs.addQuestion(new Question("Which country is considered a renegade province by China?", new String[]{"Japan", "North Korea", "South Korea", "Taiwan"}, "Taiwan", 20, 10));

        //Repeat vietnams question
        qs = new QuestionSet();
        questionSets.put(R.drawable.flag_my, qs);//Malaysia
        qs.addQuestion(new Question("Vietnam is bounded in the southwest by", new String[]{"Japan", "Bight of Guinea", "Indonesia", "Cambodia"}, "Cambodia", 20, 10));
        qs.addQuestion(new Question("What is the capital city of the country?", new String[]{"Nha Trang", "Hue", "Da Nang", "Hanoi"}, "Hanoi", 15, 5));
        qs.addQuestion(new Question("As of 2018, what percentage of the Vietnamese population practice Christianity?", new String[]{"10.8 percent", "19 percent", "8.3 percent"}, "8.3 percent", 15, 5));
        qs.addQuestion(new Question("When did the country gain independence?", new String[]{"May 13, 1949", "January 1941", "July 9, 1947", "September 2, 1945"}, "September 2, 1945", 20, 10));
        qs.addQuestion(new Question("What percentage of the Vietnamese territory is made up of water?", new String[]{"8.3 percent", "6.4 percent"}, "6.4 percent", 20, 10));
        qs.addQuestion(new Question("When did northern Vietnam cease being a Chinese territory?", new String[]{"AD 781", "AD 939", "AD 949", "AD 780"}, "AD 939", 30, 15, true));

        //From https://go4quiz.com/category/Japan/
        qs = new QuestionSet();
        questionSets.put(R.drawable.flag_jp, qs);//Japan
        qs.addQuestion(new Question("When was Eisaku Sato Prime Minister of Japan?", new String[]{"1943-1948", "1964-1972", "1974-1977", "1969-1973"}, "1964-1972", 20, 10));
        qs.addQuestion(new Question("When was Eisaku Sato born?", new String[]{"27 March 1901", "1 April 1900", "9 August 1902", "12 December 1903"}, "27 March 1901", 20, 10));
        qs.addQuestion(new Question("Where was Eisaku Sato born?", new String[]{"Osaka", "Kyoto", "Fukuoka", "Tabuse"}, "Tabuse", 20, 10));
        qs.addQuestion(new Question("From which university did Eisaku Sato graduate?", new String[]{"Okinawa University", "Tokyo Imperial University", "Nara University", "Kobe University"}, "Tokyo Imperial University", 20, 10));
        qs.addQuestion(new Question("What was Eisaku Sato’s portfolio in 1952?", new String[]{"Home", "Finance", "Construction", "Defence"}, "Construction", 20, 10));
        qs.addQuestion(new Question("Which party did Eisaku Sato join in 1948?", new String[]{"Conservative", "Liberal", "Progressive", "Republican"}, "Liberal", 20, 10));
        qs.addQuestion(new Question("Which islands were returned by USA to Japan in 1972?", new String[]{"Hawaii", "Ryukyu", "Christmas", "Paracel"}, "Ryukyu", 20, 10));
        qs.addQuestion(new Question("When did Eisaku Sato get Nobel Prize for Peace?", new String[]{"1986", "1977", "1974", "1994"}, "1974", 20, 10));
        qs.addQuestion(new Question("When did Eisaku Sato die?", new String[]{"23 February 1984", "3 June 1975", "5 July 1979", "6 November 1995"}, "3 June 1975", 20, 10));
        qs.addQuestion(new Question("Where did Eisaku Sato die?", new String[]{"Tokyo", "Akita", "Kochi", "Nagasaki"}, "Tokyo", 20, 10));

        //From https://go4quiz.com/category/Italy/
        qs = new QuestionSet();
        questionSets.put(R.drawable.flag_it, qs);//Italy
        qs.addQuestion(new Question("When did Giuseppe Piazzi discover Ceres?", new String[]{"1 January 1801", "6 May 1804", "4 August 1809", "8 November 1805"}, "1 January 1801", 20, 10));
        qs.addQuestion(new Question("When was Giuseppe Piazzi born?", new String[]{"2 February 1756", "4 April 1751", "16 July 1746", "9 December 1741"}, "16 July 1746", 20, 10));
        qs.addQuestion(new Question("Where was Giuseppe Piazzi born?", new String[]{"Turin", "Ponte de Valtellina", "Milan", "Venice"}, "Ponte de Valtellina", 20, 10));
        qs.addQuestion(new Question("Where order did Giuseppe Piazzi join?", new String[]{"Dominican", "Capuchin", "Augustinian", "Theatine"}, "Theatine", 20, 10));
        qs.addQuestion(new Question("What did Giuseppe Piazzi teach in Rome?", new String[]{"Medicine", "Theology", "Surgery", "Physics"}, "Theology", 20, 10));
        qs.addQuestion(new Question("How many stars did Giuseppe Piazzi mention in his catalogue?", new String[]{"7,646", "1,248", "8,421", "4,568"}, "7,646", 20, 10));
        qs.addQuestion(new Question("Where did Giuseppe Piazzi teach higher mathematics?", new String[]{"Paris", "Naples", "Palermo", "Geneva"}, "Palermo", 20, 10));
        qs.addQuestion(new Question("What did Giuseppe Piazzi found in Palermo?", new String[]{"Hospital", "Priory", "Abbey", "Observatory"}, "Observatory", 20, 10));
        qs.addQuestion(new Question("When did Giuseppe Piazzi die?", new String[]{"16 March 1864", "20 June 1855", "22 July 1826", "12 October 1842"}, "22 July 1826", 20, 10));
        qs.addQuestion(new Question("Where did Giuseppe Piazzi die?", new String[]{"Padua", "Ravenna", "Viterbo", "Naples"}, "Naples", 20, 10));

        //Repeat Vietnam's Questions
        qs = new QuestionSet();
        questionSets.put(R.drawable.flag_hk, qs);
        qs.addQuestion(new Question("Vietnam is bounded in the southwest by", new String[]{"Japan", "Bight of Guinea", "Indonesia", "Cambodia"}, "Cambodia", 20, 10));
        qs.addQuestion(new Question("What is the capital city of the country?", new String[]{"Nha Trang", "Hue", "Da Nang", "Hanoi"}, "Hanoi", 15, 5));
        qs.addQuestion(new Question("As of 2018, what percentage of the Vietnamese population practice Christianity?", new String[]{"10.8 percent", "19 percent", "8.3 percent"}, "8.3 percent", 15, 5));
        qs.addQuestion(new Question("When did the country gain independence?", new String[]{"May 13, 1949", "January 1941", "July 9, 1947", "September 2, 1945"}, "September 2, 1945", 20, 10));
        qs.addQuestion(new Question("What percentage of the Vietnamese territory is made up of water?", new String[]{"8.3 percent", "6.4 percent"}, "6.4 percent", 20, 10));
        qs.addQuestion(new Question("When did northern Vietnam cease being a Chinese territory?", new String[]{"AD 781", "AD 939", "AD 949", "AD 780"}, "AD 939", 30, 15, true));

        //From https://go4quiz.com/category/Germany/
        qs = new QuestionSet();
        questionSets.put(R.drawable.flag_de, qs);//Germany
        qs.addQuestion(new Question("When was Alfred Lothar Wegener born?", new String[]{"3 March 1888", "1 May 1884", "2 July 1882", "1 November 1880"}, "1 November 1880", 20, 10));
        qs.addQuestion(new Question("Where was Alfred Lothar Wegener born?", new String[]{"Innsbruck", "Berlin", "Hamburg", "Dresden"}, "Berlin", 20, 10));
        qs.addQuestion(new Question("From which university did Alfred Lothar Wegener get doctorate in astronomy?", new String[]{"Ruperto-Carola", "Loughborough", "Johann Wolfgang Goethe", "Friedrich Wilhelms"}, "Friedrich Wilhelms", 20, 10));
        qs.addQuestion(new Question("When was Alfred Lothar Wegener’s first expedition to Greenland?", new String[]{"1906-1908", "1896-1900", "1902-1904", "1914-1918"}, "1906-1908", 20, 10));
        qs.addQuestion(new Question("What did Alfred Lothar Wegener build near Danmarkshavn?", new String[]{"Planetarium", "Observatory", "Meteorological station", "College"}, "Meteorological station", 20, 10));
        qs.addQuestion(new Question("At which university did Alfred Lothar Wegener teach in 1908-1914?", new String[]{"Heidelberg", "Marburg", "Dusseldorf", "Danzig"}, "Marburg", 20, 10));
        qs.addQuestion(new Question("When did Alfred Lothar Wegener publish Die Entstehung der Kontinente und Ozeane (The Origin of Continents and Oceans)?", new String[]{"1910", "1905", "1915", "1908"}, "1915", 20, 10));
        qs.addQuestion(new Question("What did Alfred Lothar Wegener call the single large continent that broke up?", new String[]{"Urkontinent", "Laurasia", "Gondwana", "Azania"}, "Urkontinent", 20, 10));
        qs.addQuestion(new Question("Which theory did Alfred Lothar Wegener propose?", new String[]{"Evolution", "Relativity", "Continental drift", "Dark matter"}, "Continental drift", 20, 10));
        qs.addQuestion(new Question("When was Alfred Lothar Wegener professor of meteorology and geophysics at the University of Graz?", new String[]{"1932-1938", "1924-1930", "1940-1944", "1936-1940"}, "1924-1930", 20, 10));

        //From https://go4quiz.com/category/canada/
        qs = new QuestionSet();
        questionSets.put(R.drawable.flag_ca, qs);//Canada
        qs.addQuestion(new Question("What was Mary Pickford’s original name?", new String[]{"Gladys Mary Smith", "Helen Marguerite Clark", "Lillian Dana Gish", "Frances Marion Owens"}, "Gladys Mary Smith", 20, 10));
        qs.addQuestion(new Question("When was Mary Pickford born?", new String[]{"10 February 1894", "9 April 1893", "2 September 1892", "7 December 1891"}, "9 April 1893", 20, 10));
        qs.addQuestion(new Question("Where was Mary Pickford born?", new String[]{"Montreal", "Ottawa", "Halifax", "Toronto"}, "Toronto", 20, 10));
        qs.addQuestion(new Question("When did Mary Pickford act in The Lonely Villa?", new String[]{"1905", "1902", "1909", "1912"}, "1909", 20, 10));
        qs.addQuestion(new Question("Which film established Mary Pickford as America’s Sweetheart?", new String[]{"Tess of the Storm Country", "The Taming of the Shrew", "Pollyanna", "My Best Girl"}, "Tess of the Storm Country", 20, 10));
        qs.addQuestion(new Question("When was Mary Pickford married to Douglas Fairbanks?", new String[]{"1911-1919", "1937-1956", "1920-1935", "1960-1984"}, "1920-1935", 20, 10));
        qs.addQuestion(new Question("Which was Mary Pickford’s last film as an actress?", new String[]{"Daddy Long Legs", "Secrets", "Hearts Adrift", "Poor Little Rich Girl"}, "Secrets", 20, 10));
        qs.addQuestion(new Question("When was Sunshine and Shadow published?", new String[]{"1948", "1945", "1952", "1955"}, "1955", 20, 10));
        qs.addQuestion(new Question("When did Mary Pickford die?", new String[]{"12 January 1984", "28 May 1979", "17 July 1983", "14 November 1985"}, "28 May 1979", 20, 10));
        qs.addQuestion(new Question("Where did Mary Pickford die?", new String[]{"Tacoma", "Everett", "Bremerton", "Santa Monica"}, "Santa Monica", 20, 10));

        //The next 4 flags contains questions from Vietnam flag
        qs = new QuestionSet();
        questionSets.put(R.drawable.flag_br, qs);//Brazil
        qs.addQuestion(new Question("Vietnam is bounded in the southwest by", new String[]{"Japan", "Bight of Guinea", "Indonesia", "Cambodia"}, "Cambodia", 20, 10));
        qs.addQuestion(new Question("What is the capital city of the country?", new String[]{"Nha Trang", "Hue", "Da Nang", "Hanoi"}, "Hanoi", 15, 5));
        qs.addQuestion(new Question("As of 2018, what percentage of the Vietnamese population practice Christianity?", new String[]{"10.8 percent", "19 percent", "8.3 percent"}, "8.3 percent", 15, 5));
        qs.addQuestion(new Question("When did the country gain independence?", new String[]{"May 13, 1949", "January 1941", "July 9, 1947", "September 2, 1945"}, "September 2, 1945", 20, 10));
        qs.addQuestion(new Question("What percentage of the Vietnamese territory is made up of water?", new String[]{"8.3 percent", "6.4 percent"}, "6.4 percent", 20, 10));
        qs.addQuestion(new Question("When did northern Vietnam cease being a Chinese territory?", new String[]{"AD 781", "AD 939", "AD 949", "AD 780"}, "AD 939", 30, 15, true));

        qs = new QuestionSet();
        questionSets.put(R.drawable.flag_bd, qs);//Belgium
        qs.addQuestion(new Question("Vietnam is bounded in the southwest by", new String[]{"Japan", "Bight of Guinea", "Indonesia", "Cambodia"}, "Cambodia", 20, 10));
        qs.addQuestion(new Question("What is the capital city of the country?", new String[]{"Nha Trang", "Hue", "Da Nang", "Hanoi"}, "Hanoi", 15, 5));
        qs.addQuestion(new Question("As of 2018, what percentage of the Vietnamese population practice Christianity?", new String[]{"10.8 percent", "19 percent", "8.3 percent"}, "8.3 percent", 15, 5));
        qs.addQuestion(new Question("When did the country gain independence?", new String[]{"May 13, 1949", "January 1941", "July 9, 1947", "September 2, 1945"}, "September 2, 1945", 20, 10));
        qs.addQuestion(new Question("What percentage of the Vietnamese territory is made up of water?", new String[]{"8.3 percent", "6.4 percent"}, "6.4 percent", 20, 10));
        qs.addQuestion(new Question("When did northern Vietnam cease being a Chinese territory?", new String[]{"AD 781", "AD 939", "AD 949", "AD 780"}, "AD 939", 30, 15, true));

        qs = new QuestionSet();
        questionSets.put(R.drawable.flag_af, qs);//Afghanistan
        qs.addQuestion(new Question("Vietnam is bounded in the southwest by", new String[]{"Japan", "Bight of Guinea", "Indonesia", "Cambodia"}, "Cambodia", 20, 10));
        qs.addQuestion(new Question("What is the capital city of the country?", new String[]{"Nha Trang", "Hue", "Da Nang", "Hanoi"}, "Hanoi", 15, 5));
        qs.addQuestion(new Question("As of 2018, what percentage of the Vietnamese population practice Christianity?", new String[]{"10.8 percent", "19 percent", "8.3 percent"}, "8.3 percent", 15, 5));
        qs.addQuestion(new Question("When did the country gain independence?", new String[]{"May 13, 1949", "January 1941", "July 9, 1947", "September 2, 1945"}, "September 2, 1945", 20, 10));
        qs.addQuestion(new Question("What percentage of the Vietnamese territory is made up of water?", new String[]{"8.3 percent", "6.4 percent"}, "6.4 percent", 20, 10));
        qs.addQuestion(new Question("When did northern Vietnam cease being a Chinese territory?", new String[]{"AD 781", "AD 939", "AD 949", "AD 780"}, "AD 939", 30, 15, true));

        qs = new QuestionSet();
        questionSets.put(R.drawable.flag_ch, qs);//Switzerland
        qs.addQuestion(new Question("Vietnam is bounded in the southwest by", new String[]{"Japan", "Bight of Guinea", "Indonesia", "Cambodia"}, "Cambodia", 20, 10));
        qs.addQuestion(new Question("What is the capital city of the country?", new String[]{"Nha Trang", "Hue", "Da Nang", "Hanoi"}, "Hanoi", 15, 5));
        qs.addQuestion(new Question("As of 2018, what percentage of the Vietnamese population practice Christianity?", new String[]{"10.8 percent", "19 percent", "8.3 percent"}, "8.3 percent", 15, 5));
        qs.addQuestion(new Question("When did the country gain independence?", new String[]{"May 13, 1949", "January 1941", "July 9, 1947", "September 2, 1945"}, "September 2, 1945", 20, 10));
        qs.addQuestion(new Question("What percentage of the Vietnamese territory is made up of water?", new String[]{"8.3 percent", "6.4 percent"}, "6.4 percent", 20, 10));
        qs.addQuestion(new Question("When did northern Vietnam cease being a Chinese territory?", new String[]{"AD 781", "AD 939", "AD 949", "AD 780"}, "AD 939", 30, 15, true));
    }

    public static Game getInstance()
    {
        if (game == null)
        {
            game = new Game();
        }

        return game;
    }

    /**
     * When EndGame button is pressed in screen 1 the app will exit that the first screen's activity
     * will call finish(). However, when the app is opened again the singleton retains its previous
     * state, that the app's memory is not collected even though it is finished. Therefore, this
     * is used to create a new singleton instance for the app.
     */
    public static void createNewInstance()
    {
        game = new Game();
    }

    public boolean isSpecialQuestionAnswered()
    {
        return specialQuestionAnswered;
    }

    public void setSpecialQuestionAnswered(boolean bool)
    {
        this.specialQuestionAnswered = bool;
    }

    public int getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(int currentPoint) {
        this.currentPoint = currentPoint;
    }

    public int getWinningPoint() {
        return winningPoint;
    }

    public void setWinningPoint(int winningPoint) {
        this.winningPoint = winningPoint;
    }

    public List<Integer> getFlagIdList()
    {
        return new ArrayList<>(questionSets.keySet());
    }

    public QuestionSet getCurrentQuestionSet()
    {
        return currentQuestionSet;
    }

    public QuestionSet getQuestionSet(int key)
    {
        return questionSets.get(key);
    }

    public void setCurrentQuestionSet(int flagId)
    {
        currentQuestionSet = questionSets.get(flagId);
    }

    /**
     * game is considered a end when current point is greater or equal to the winning point or when
     * current point is smaller or equal to 0
     *
     * @return whether the game ended or not
     */
    public boolean isGameEnded()
    {
        return currentPoint <= 0 || currentPoint >= winningPoint;
    }
}
