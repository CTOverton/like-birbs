package edu.psu.cto5068.like_birbs;

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
                                "Donna",
                                "Gerold",
                                "Edward",
                                "Alphonse",
                                "Roy",
        "Lucy","Antonio","Ezra","Alois","Sasha","Louise","Sebastian","Ciel","Maria","Wendy","Giotto","Gilbert","Gray","Mavis","Makarov","Mirajane","Winry",
        "Beatrice","Frau","Rosette","Oz","Elliot","Glen","Oscar","Rufus","Grell","Tsuna","Takeshi","Hayato","Reborn","Kyoya","Ryohei","Kyoko","Minfilia",
        "Lambo","Mukuro","Chrome","Xanxus","Superbi","Lussuria","Leviathan","Belphegor","Fran","Yuni","Byakuran","Daemon","Alaude","Knuckle","G.","Ugetsu","Lampo",
        "Ieyasu","Timoteo","Luce","Colonello","Skull","Viper","Lal","Fong","Verde","Iemitsu","Basil","Shoichi","Tetsuya","Cozart","Enma","Haru","Hana","Nana","Bianchi",
        "Shamal","I-Pin","Dino","Futa","Sepira","Freya","Hayate","Mirage","Mikumo","Kaname","Makina","Reina","Arad","Messer","Keith","Theo","Kassim","Alto","Ranka",
        "Sheryl"};
    }

    public String getRandomName() {
        int randomNameIndex = (int) (Math.random() * nameList.length);
        return nameList[randomNameIndex];
    }
}
