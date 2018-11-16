
/**
 * Database (very underdeveloped)
 */
public class Database {

    // we should replace these with our own data structures later
    private final String[] reasons; //different reasons for sign in
    private final String[] subjects; //different subjects for sign in


    public Database() {

        //instead of hardcoding reasons or subjects, we could read from a .txt file to make it more dynamic
        reasons = new String[] {
                "Test", "Chill Zone", "Quiet Work", "Academic Support", "Group Work"
        };

        subjects = new String[] {
                "Art", "Math", "Music", "Science", "History", "Geography", "Business", "Family Studies",
                "Physical Ed.", "Technology Studies", "Social Sciences", "Lunch / Spare"
        };
    }

    public String[] getReasons() {
        return reasons;
    }

    public String[] getSubjects() {
        return subjects;
    }

    /**
     * Empty sign in method
     * Should throw some errors later to be fancy (so we can display the message)
     * @param id
     * @param course
     * @param reason
     * @return true if the signing was successful
     */
    public boolean signIn(String id, String course, String reason) {
        return true;
    }

    /**
     * Empty sign out method
     * Should throw some errors later too
     * @param id
     * @return true if the signing out was successful
     */
    public boolean signOut(String id) {
        return true;
    }


}