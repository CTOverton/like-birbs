public class BirbNameGenerator {
    private String[] nameList;

    public BirbNameGenerator() {
        nameList = new String[]{"Jacob",
                                "Christian",
                                "Ariel",
                                "Josh",
                                "Richard",
                                "Jon",
                                "Alyssa",
                                "Sam",
                                "Jeremy",
                                "Danielle",
                                "Zack",
                                "Aidan",
                                "Ben",
                                "Chris",
                                "Terry",
                                "Zach",
                                "Sean",
                                "Kenny",
                                "Moses",
                                "Mitchel",
                                "Jenn",
                                "Mya",
                                "Linda",
                                "Sukmoon",
                                "Hein",
                                "Hyuante",
                                "David",
                                "Dontez",
                                "Richardo",
                                "Timeteo",
                                "Victor",
                                "Sulema",
                                "Ulfric",
                                "Doom",
                                "Varg",
                                "Steve",
                                "Charlie",
                                "Dee",
                                "Dennis",
                                "Joe",
                                "Patricia",
                                "Robert",
                                "James",
                                "Donna"};
    }

    public String getRandomName() {
        int randomNameIndex = (int) (Math.random() * nameList.length);
        return nameList[randomNameIndex];
    }
}
