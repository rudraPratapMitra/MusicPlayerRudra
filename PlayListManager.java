package cwh;
import java.io.*;
import java.util.Scanner;

class Node {
    String song;
    Node next;
    Node prev;

    Node(String song) {
        this.song = song;
        this.next = null;
        this.prev = null;
    }
}

public class PlayListManager {
    static Node top = null;
    //static Node top1 = null;

    public static void toFile(String a) {
        try {
            FileWriter fw = new FileWriter("playlist.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(a);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addNode(Node first) {
        Scanner scanner = new Scanner(System.in);
        String a;
        while (first.next != null) {
            first = first.next;
        }
        first.next = new Node(null); // Initialize with null song name
        first.next.prev = first;
        first = first.next;
        System.out.println("\nEnter Song name: ");
        a = scanner.nextLine();
        first.song = a;
        toFile(a);
        first.next = null;
    }


    public static void deleteFile(String a) {
        try {
            File inputFile = new File("playlist.txt");
            File tempFile = new File("temp.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                if (!currentLine.equals(a)) {
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
            }
            writer.close();
            reader.close();

            boolean successful = tempFile.renameTo(inputFile);
            if (!successful) {
                System.out.println("Error deleting the song.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteNode(Node first, String song) {
        Node current = first;
        while (current != null) {
            if (current.song.equals(song)) {
                if (current.prev != null) {
                    current.prev.next = current.next;
                }
                if (current.next != null) {
                    current.next.prev = current.prev;
                }
                deleteFile(song);
                System.out.println("Song has been deleted.");
                return;
            }
            current = current.next;
        }
        System.out.println("There is no song with the name you entered.");
    }

    public static void printList(Node first) {
        System.out.println("\nPlaylist Name: " + first.song);
        first = first.next; // Skip the playlist name
        while (first != null) {
            System.out.println(first.song);
            first = first.next;
        }
    }


    public static void countNodes(Node first) {
        int count = 0;
        first = first.next; // Skip the playlist name
        while (first != null) {
            count++;
            first = first.next;
        }
        System.out.println("\nTotal songs: " + count);
    }



    public static void searchSong(Node first, String songToSearch) {
        boolean found = false;
        while (first != null) {
            if (first.song.equals(songToSearch)) {
                System.out.println("\nSong Found: " + songToSearch);
                found = true;
                break; // Exit the loop since the song is found
            }
            first = first.next;
        }
        if (!found) {
            System.out.println("\nSong Not found");
        }
    }

    public static void playSong(Node first, String songToPlay) {
        boolean found = false;
        while (first != null) {
            if (first.song.equals(songToPlay)) {
                System.out.println("\nNow Playing: " + songToPlay);
                found = true;
                // Implement logic for tracking last played song
                // push(songToPlay);
                break; // Exit the loop since the song is found
            }
            first = first.next;
        }
        if (!found) {
            System.out.println("\nSong Not found");
        }
    }

    // Implement the 'recent' method to display recently played songs
    public static void recent() {
        Node current = top;
        if (current == null) {
            System.out.println("\n**No recently played tracks.**");
            return;
        }

        System.out.println("\n**Recently played tracks:**");
        while (current != null) {
            System.out.println(current.song);
            current = current.next;
        }
    }

    // Implement the 'topelement' method to display the last played song
    public static void topelement() {
        if (top == null) {
            System.out.println("\n**No last played tracks.**");
        } else {
            System.out.println("\n**Last Played Song: " + top.song + "**");
        }
    }

    // Implement a sorting algorithm for sorting the playlist
    public static void sort(Node pointer) {
        Node a, b, e = null;
        String tmp;

        if (pointer == null) {
            return;
        }

        while (e != pointer.next) {
            a = pointer;
            b = a.next;

            while (b != e) {
                if (a.song.compareTo(b.song) > 0) {
                    tmp = a.song;
                    a.song = b.song;
                    b.song = tmp;
                }
                a = b;
                b = b.next;
            }
            e = a;
        }
    }



    public static void addPlaylist(Node start) {
        try {
            FileReader fr = new FileReader("playlist.txt");
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                addNode(start);
                start.next.song = line;
            }
            br.close();
            System.out.println("Playlist Added");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteMenu(Node start) {
        Scanner scanner = new Scanner(System.in);
        int c;
        System.out.println("Which type of delete do you want?");
        System.out.println("1. By Search");
        System.out.println("2. By Position");
        c = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        switch (c) {
            case 1:
                System.out.println("\nEnter the name of the song to delete: ");
                String songToDelete = scanner.nextLine();
                deleteNode(start, songToDelete);
                break;
            case 2:
                System.out.println("\nEnter the position of the song to delete: ");
                int pos = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
                deletePosition(start, pos);
                break;
        }
    }

    public static void deletePosition(Node pointer, int pos) {
        if (pos < 1) {
            System.out.println("\nInvalid position. Position must be a positive integer.");
            return;
        }

        if (pos == 1) {
            // Deleting the first song
            if (pointer != null) {
                deleteFile(pointer.song); // Delete the song from the file
                if (pointer.next != null) {
                    pointer.next.prev = null;
                }
                pointer = pointer.next; // This assignment is not needed and can be removed
                System.out.println("\nSong has been deleted.");
            } else {
                System.out.println("\nThere are no songs to delete.");
            }
            return;
        }

        int currentPos = 1;
        Node prev = null;
        while (pointer != null && currentPos < pos) {
            prev = pointer;
            pointer = pointer.next;
            currentPos++;
        }

        if (pointer == null) {
            System.out.println("\nSong not found at position " + pos + ".");
        } else {
            deleteFile(pointer.song); // Delete the song from the file
            prev.next = pointer.next;
            if (pointer.next != null) {
                pointer.next.prev = prev;
            }
            System.out.println("\nSong has been deleted.");
        }
    }


    public static void main(String[] args) {
        int choice;
        Node start, hold;
        Scanner scanner = new Scanner(System.in);
        start = new Node(null);
        System.out.println("\t\t\t**WELCOME**");
        System.out.println("**please use '_' for space.");
        System.out.print("\nEnter your playlist name: ");
        start.song = scanner.nextLine();
        start.next = null;
        hold = start;

        do {
            System.out.println("\n1. Add New Song");
            System.out.println("2. Delete Song");
            System.out.println("3. Display Entered Playlist");
            System.out.println("4. Total Songs");
            System.out.println("5. Search Song");
            System.out.println("6. Play Song");
            System.out.println("7. Recently Played List");
            System.out.println("8. Last Played");
            System.out.println("9. Shuffle");
            System.out.println("10. Add From File");
            System.out.println("11. Exit");
            System.out.print("\nEnter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    addNode(start);
                    break;
                case 2:
                    deleteMenu(start);
                    break;
                case 3:
                    printList(start);
                    break;
                case 4:
                    countNodes(hold);
                    break;
                case 5:
                    System.out.print("\nEnter the song to search: ");
                    String songToSearch = scanner.nextLine();
                    searchSong(start, songToSearch);
                    break;
                case 6:
                    System.out.print("\nEnter the song to play: ");
                    String songToPlay = scanner.nextLine();
                    playSong(start, songToPlay);
                    break;
                case 7:
                    System.out.println("\n **Recently Played**");
                    recent();
                    break;

                case 8:
                    System.out.println("\n**Last Played**");
                    topelement();
                    break;
                case 9:
                    sort(start.next);
                    System.out.println("\n**Playlist Shuffled**");
                    break;

                case 10:
                    addPlaylist(start);
                    break;
                case 11:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        } while (choice != 11);
    }
}
