import java.util.Scanner;

/* =========================================================================
   HOSPITAL MANAGEMENT SYSTEM
   ---------------------------------------------------------------------
   Demonstrates 4 core data structures:
     1. Binary Search Tree (BST)   -> Patient Records          (keyed by ID)
     2. Queue (linked list based)  -> Emergency Patient Queue  (FIFO)
     3. Stack (linked list based)  -> Treatment History        (LIFO)
     4. Singly Linked List         -> Per-Patient Visit History
   ========================================================================= */

public class HospitalManagementSystem {

    /*
     * =====================================================================
     * 0. SUPPORT CLASS: Visit (node payload for the Visit History list)
     * =====================================================================
     */
    static class Visit {
        int visitId;
        String visitDate;
        String doctorName;
        String diagnosis;
        String treatment;

        Visit(int visitId, String visitDate, String doctorName, String diagnosis, String treatment) {
            this.visitId = visitId;
            this.visitDate = visitDate;
            this.doctorName = doctorName;
            this.diagnosis = diagnosis;
            this.treatment = treatment;
        }

        @Override
        public String toString() {
            return "    Visit ID: " + visitId +
                    " | Date: " + visitDate +
                    " | Doctor: " + doctorName +
                    " | Diagnosis: " + diagnosis +
                    " | Treatment: " + treatment;
        }
    }

    /*
     * =====================================================================
     * 4. SINGLY LINKED LIST -> Patient Visit History
     * =====================================================================
     */
    static class VisitNode {
        Visit data;
        VisitNode next;

        VisitNode(Visit data) {
            this.data = data;
        }
    }

    static class VisitHistoryList {
        private VisitNode head;
        private int size;

        // Add a new visit at the end of the list
        boolean addVisit(Visit v) {
            if (searchVisit(v.visitId) != null)
                return false;
            VisitNode newNode = new VisitNode(v);
            if (head == null) {
                head = newNode;
                size++;
                return true;
            }
            VisitNode temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
            size++;
            return true;
        }

        // Remove a visit by Visit ID
        Visit removeVisit(int visitId) {
            if (head == null)
                return null;

            if (head.data.visitId == visitId) {
                Visit removed = head.data;
                head = head.next;
                size--;
                return removed;
            }

            VisitNode prev = head;
            VisitNode curr = head.next;
            while (curr != null) {
                if (curr.data.visitId == visitId) {
                    Visit removed = curr.data;
                    prev.next = curr.next;
                    size--;
                    return removed;
                }
                prev = curr;
                curr = curr.next;
            }
            return null;
        }

        // Search for a visit by Visit ID
        Visit searchVisit(int visitId) {
            VisitNode temp = head;
            while (temp != null) {
                if (temp.data.visitId == visitId) {
                    return temp.data;
                }
                temp = temp.next;
            }
            return null;
        }

        // Display the full visit history
        void display() {
            if (head == null) {
                System.out.println("    No visit history available.");
                return;
            }
            VisitNode temp = head;
            while (temp != null) {
                System.out.println(temp.data);
                temp = temp.next;
            }
        }

        boolean isEmpty() {
            return head == null;
        }

        int size() {
            return size;
        }
    }

    /*
     * =====================================================================
     * SUPPORT CLASS: Patient (payload stored in the BST, and in the Queue)
     * =====================================================================
     */
    static class Patient {
        int patientId;
        String name;
        int age;
        String contact;
        String medicalCondition;
        VisitHistoryList visitHistory; // each patient owns a singly linked list

        Patient(int patientId, String name, int age, String contact, String medicalCondition) {
            this.patientId = patientId;
            this.name = name;
            this.age = age;
            this.contact = contact;
            this.medicalCondition = medicalCondition;
            this.visitHistory = new VisitHistoryList();
        }

        @Override
        public String toString() {
            return "Patient ID: " + patientId +
                    " | Name: " + name +
                    " | Age: " + age +
                    " | Contact: " + contact +
                    " | Condition: " + medicalCondition;
        }
    }

    /*
     * =====================================================================
     * 1. BINARY SEARCH TREE -> Patient Records (keyed by Patient ID)
     * =====================================================================
     */
    static class BSTNode {
        Patient data;
        BSTNode left, right;

        BSTNode(Patient data) {
            this.data = data;
        }
    }

    static class PatientBST {
        private BSTNode root;
        private int size;

        // ---- Insert ----
        boolean insert(Patient p) {
            if (search(p.patientId) != null)
                return false;
            root = insertRec(root, p);
            size++;
            return true;
        }

        private BSTNode insertRec(BSTNode node, Patient p) {
            if (node == null)
                return new BSTNode(p);
            if (p.patientId < node.data.patientId) {
                node.left = insertRec(node.left, p);
            } else if (p.patientId > node.data.patientId) {
                node.right = insertRec(node.right, p);
            }
            return node;
        }

        // ---- Search ----
        Patient search(int patientId) {
            return searchRec(root, patientId);
        }

        private Patient searchRec(BSTNode node, int patientId) {
            if (node == null)
                return null;
            if (patientId == node.data.patientId)
                return node.data;
            return patientId < node.data.patientId
                    ? searchRec(node.left, patientId)
                    : searchRec(node.right, patientId);
        }

        // ---- Delete ----
        boolean delete(int patientId) {
            if (search(patientId) == null)
                return false;
            root = deleteRec(root, patientId);
            size--;
            return true;
        }

        private BSTNode deleteRec(BSTNode node, int patientId) {
            if (node == null)
                return null;

            if (patientId < node.data.patientId) {
                node.left = deleteRec(node.left, patientId);
            } else if (patientId > node.data.patientId) {
                node.right = deleteRec(node.right, patientId);
            } else {
                // Node found
                if (node.left == null)
                    return node.right;
                if (node.right == null)
                    return node.left;

                // Two children: replace with in-order successor (smallest in right subtree)
                BSTNode successor = findMin(node.right);
                node.data = successor.data;
                node.right = deleteRec(node.right, successor.data.patientId);
            }
            return node;
        }

        private BSTNode findMin(BSTNode node) {
            while (node.left != null)
                node = node.left;
            return node;
        }

        // ---- In-order traversal (ascending Patient ID) ----
        void inorderDisplay() {
            if (root == null) {
                System.out.println("No patient records found.");
                return;
            }
            inorderRec(root);
        }

        private void inorderRec(BSTNode node) {
            if (node == null)
                return;
            inorderRec(node.left);
            System.out.println(node.data);
            inorderRec(node.right);
        }

        boolean isEmpty() {
            return root == null;
        }

        int size() {
            return size;
        }
    }

    /*
     * =====================================================================
     * 2. QUEUE (linked list based) -> Emergency Patient Queue (FIFO)
     * =====================================================================
     */
    static class QueueNode {
        Patient data;
        QueueNode next;

        QueueNode(Patient data) {
            this.data = data;
        }
    }

    static class EmergencyQueue {
        private QueueNode front, rear;
        private int size = 0;

        // ---- Enqueue ----
        boolean enqueue(Patient p) {
            if (containsPatient(p.patientId)) {
                System.out.println("Patient is already waiting in the emergency queue.");
                return false;
            }
            QueueNode newNode = new QueueNode(p);
            if (rear == null) {
                front = rear = newNode;
            } else {
                rear.next = newNode;
                rear = newNode;
            }
            size++;
            System.out.println("Patient \"" + p.name + "\" (ID: " + p.patientId + ") added to emergency queue.");
            return true;
        }

        // ---- Dequeue ----
        Patient dequeue() {
            if (isEmpty()) {
                System.out.println("Emergency queue is empty. No patient to treat.");
                return null;
            }
            Patient p = front.data;
            front = front.next;
            if (front == null)
                rear = null;
            size--;
            return p;
        }

        // ---- Display ----
        void display() {
            if (isEmpty()) {
                System.out.println("Emergency queue is currently empty.");
                return;
            }
            System.out.println("Patients waiting (front -> rear, total: " + size + "):");
            QueueNode temp = front;
            int position = 1;
            while (temp != null) {
                System.out.println(position + ". " + temp.data);
                temp = temp.next;
                position++;
            }
        }

        boolean removePatient(int patientId) {
            QueueNode previous = null;
            QueueNode current = front;
            while (current != null) {
                if (current.data.patientId == patientId) {
                    if (previous == null)
                        front = current.next;
                    else
                        previous.next = current.next;
                    if (current == rear)
                        rear = previous;
                    size--;
                    return true;
                }
                previous = current;
                current = current.next;
            }
            return false;
        }

        boolean isEmpty() {
            return front == null;
        }

        boolean containsPatient(int patientId) {
            QueueNode current = front;
            while (current != null) {
                if (current.data.patientId == patientId)
                    return true;
                current = current.next;
            }
            return false;
        }

        int size() {
            return size;
        }
    }

    /*
     * =====================================================================
     * SUPPORT CLASS: TreatmentRecord (payload for the Treatment History stack)
     * =====================================================================
     */
    static class TreatmentRecord {
        int patientId;
        String patientName;
        String treatmentDetails;
        String dateCompleted;

        TreatmentRecord(int patientId, String patientName, String treatmentDetails, String dateCompleted) {
            this.patientId = patientId;
            this.patientName = patientName;
            this.treatmentDetails = treatmentDetails;
            this.dateCompleted = dateCompleted;
        }

        @Override
        public String toString() {
            return "Patient ID: " + patientId +
                    " | Name: " + patientName +
                    " | Treatment: " + treatmentDetails +
                    " | Completed On: " + dateCompleted;
        }
    }

    /*
     * =====================================================================
     * 3. STACK (linked list based) -> Treatment History (LIFO)
     * =====================================================================
     */
    static class StackNode {
        TreatmentRecord data;
        StackNode next;

        StackNode(TreatmentRecord data) {
            this.data = data;
        }
    }

    static class TreatmentHistoryStack {
        private StackNode top;
        private int size = 0;

        // ---- Push ----
        void push(TreatmentRecord record) {
            StackNode newNode = new StackNode(record);
            newNode.next = top;
            top = newNode;
            size++;
            System.out.println("Treatment record for \"" + record.patientName + "\" pushed onto history stack.");
        }

        // ---- Pop ----
        TreatmentRecord pop() {
            if (isEmpty()) {
                System.out.println("Treatment history stack is empty. Nothing to pop.");
                return null;
            }
            TreatmentRecord record = top.data;
            top = top.next;
            size--;
            return record;
        }

        TreatmentRecord peek() {
            if (isEmpty()) {
                System.out.println("Treatment history stack is empty. Nothing to peek.");
                return null;
            }
            return top.data;
        }

        // ---- Display (top -> bottom, i.e. most recent first) ----
        void display() {
            if (isEmpty()) {
                System.out.println("No treatment records available.");
                return;
            }
            System.out.println("Treatment history (most recent first):");
            StackNode temp = top;
            int position = 1;
            while (temp != null) {
                System.out.println(position + ". " + temp.data);
                temp = temp.next;
                position++;
            }
        }

        void displayForPatient(int patientId) {
            StackNode current = top;
            int position = 1;
            boolean found = false;
            while (current != null) {
                if (current.data.patientId == patientId) {
                    if (!found)
                        System.out.println("Treatment records for patient ID " + patientId + ":");
                    System.out.println(position + ". " + current.data);
                    found = true;
                    position++;
                }
                current = current.next;
            }
            if (!found)
                System.out.println("No treatment records found for patient ID " + patientId + ".");
        }

        boolean isEmpty() {
            return top == null;
        }

        int size() {
            return size;
        }
    }

    /*
     * =====================================================================
     * MAIN PROGRAM -> Menu-driven console interface
     * =====================================================================
     */
    private static final Scanner sc = new Scanner(System.in);
    private static final PatientBST patientRecords = new PatientBST();
    private static final EmergencyQueue emergencyQueue = new EmergencyQueue();
    private static final TreatmentHistoryStack treatmentHistory = new TreatmentHistoryStack();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1:
                    patientRecordsMenu();
                    break;
                case 2:
                    emergencyQueueMenu();
                    break;
                case 3:
                    treatmentHistoryMenu();
                    break;
                case 4:
                    patientVisitHistoryMenu();
                    break;
                case 5:
                    displaySystemSummary();
                    break;
                case 0:
                    running = false;
                    System.out.println("Exiting Hospital Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        sc.close();
    }

    private static void printMainMenu() {
        System.out.println("\n=============================================");
        System.out.println("       HOSPITAL MANAGEMENT SYSTEM");
        System.out.println("=============================================");
        System.out.println("1. Patient Records (BST)");
        System.out.println("2. Emergency Patient Queue (Queue)");
        System.out.println("3. Treatment History (Stack)");
        System.out.println("4. Patient Visit History (Linked List)");
        System.out.println("5. Display System Summary");
        System.out.println("0. Exit");
    }

    private static void displaySystemSummary() {
        System.out.println("\n--- System Summary ---");
        System.out.println("Registered patients: " + patientRecords.size());
        System.out.println("Patients waiting for emergency treatment: " + emergencyQueue.size());
        System.out.println("Completed treatment records: " + treatmentHistory.size());
        System.out.println("Visit histories are maintained per patient.");
    }

    /* --------------------------- 1. BST Menu --------------------------- */
    private static void patientRecordsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Patient Records (BST) ---");
            System.out.println("1. Insert new patient");
            System.out.println("2. Search patient by ID");
            System.out.println("3. Delete patient");
            System.out.println("4. Display all patients (in-order, ascending ID)");
            System.out.println("0. Back to main menu");
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1: {
                    int id = readPositiveInt("Enter Patient ID: ");
                    if (patientRecords.search(id) != null) {
                        System.out.println("A patient with ID " + id + " already exists.");
                        break;
                    }
                    String name = readRequiredString("Enter Patient Name: ");
                    int age = readPositiveInt("Enter Age: ");
                    String contact = readRequiredString("Enter Contact Number: ");
                    String condition = readRequiredString("Enter Medical Condition: ");
                    boolean inserted = patientRecords.insert(new Patient(id, name, age, contact, condition));
                    System.out.println(inserted
                            ? "Patient added successfully."
                            : "A patient with ID " + id + " already exists.");
                    break;
                }
                case 2: {
                    int id = readPositiveInt("Enter Patient ID to search: ");
                    Patient p = patientRecords.search(id);
                    System.out.println(p != null ? "Found -> " + p : "Patient not found.");
                    break;
                }
                case 3: {
                    int id = readPositiveInt("Enter Patient ID to delete: ");
                    boolean removed = patientRecords.delete(id);
                    if (removed && emergencyQueue.removePatient(id))
                        System.out.println("Patient was also removed from the emergency queue.");
                    System.out.println(removed ? "Patient deleted successfully." : "Patient not found.");
                    break;
                }
                case 4:
                    System.out.println("--- Patient List (ascending Patient ID, total: "
                            + patientRecords.size() + ") ---");
                    patientRecords.inorderDisplay();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /* --------------------------- 2. Queue Menu --------------------------- */
    private static void emergencyQueueMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Emergency Patient Queue (Queue) ---");
            System.out.println("1. Enqueue patient (add to waiting queue)");
            System.out.println("2. Dequeue patient (send next for treatment)");
            System.out.println("3. Display waiting queue");
            System.out.println("0. Back to main menu");
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1: {
                    int id = readPositiveInt("Enter Patient ID: ");
                    Patient existing = patientRecords.search(id);
                    if (existing != null) {
                        emergencyQueue.enqueue(existing);
                    } else {
                        System.out.println("Patient not found in records. Please enter details to register:");
                        String name = readRequiredString("Enter Patient Name: ");
                        int age = readPositiveInt("Enter Age: ");
                        String contact = readRequiredString("Enter Contact Number: ");
                        String condition = readRequiredString("Enter Medical Condition: ");
                        Patient p = new Patient(id, name, age, contact, condition);
                        patientRecords.insert(p); // also register in BST records
                        emergencyQueue.enqueue(p);
                    }
                    break;
                }
                case 2: {
                    Patient next = emergencyQueue.dequeue();
                    if (next != null) {
                        System.out.println("Now treating -> " + next);
                        String treatment = readRequiredString("Enter Treatment Details: ");
                        String date = readRequiredString("Enter Date Completed (e.g. 2026-09-06): ");
                        treatmentHistory.push(new TreatmentRecord(
                                next.patientId, next.name, treatment, date));
                        System.out.println("Treatment completed and added to history.");
                    }
                    break;
                }
                case 3:
                    emergencyQueue.display();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /* --------------------------- 3. Stack Menu --------------------------- */
    private static void treatmentHistoryMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Treatment History (Stack) ---");
            System.out.println("1. Push completed treatment record");
            System.out.println("2. Pop most recent treatment record");
            System.out.println("3. Display treatment history");
            System.out.println("4. Display treatment history for a patient");
            System.out.println("5. View most recent treatment record");
            System.out.println("0. Back to main menu");
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1: {
                    int id = readPositiveInt("Enter Patient ID: ");
                    String name;
                    Patient existing = patientRecords.search(id);
                    if (existing != null) {
                        name = existing.name;
                        System.out.println("Matched patient record: " + existing);
                    } else {
                        name = readRequiredString("Patient not in records. Enter Patient Name: ");
                    }
                    String treatment = readRequiredString("Enter Treatment Details: ");
                    String date = readRequiredString("Enter Date Completed (e.g. 2026-09-06): ");
                    treatmentHistory.push(new TreatmentRecord(id, name, treatment, date));
                    break;
                }
                case 2: {
                    TreatmentRecord popped = treatmentHistory.pop();
                    if (popped != null) {
                        System.out.println("Removed -> " + popped);
                    }
                    break;
                }
                case 3:
                    treatmentHistory.display();
                    break;
                case 4: {
                    int id = readPositiveInt("Enter Patient ID: ");
                    treatmentHistory.displayForPatient(id);
                    break;
                }
                case 5: {
                    TreatmentRecord latest = treatmentHistory.peek();
                    if (latest != null)
                        System.out.println("Most recent treatment -> " + latest);
                    break;
                }
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /* ---------------------- 4. Linked List Menu ---------------------- */
    private static void patientVisitHistoryMenu() {
        int id = readPositiveInt("Enter Patient ID to manage visit history: ");
        Patient p = patientRecords.search(id);
        if (p == null) {
            System.out.println("Patient not found. Please add the patient to records first (Option 1).");
            return;
        }

        boolean back = false;
        while (!back) {
            System.out.println("\n--- Visit History for " + p.name + " (ID: " + p.patientId + ") ---");
            System.out.println("1. Add new visit");
            System.out.println("2. Remove visit by Visit ID");
            System.out.println("3. Search visit by Visit ID");
            System.out.println("4. Display full visit history");
            System.out.println("0. Back to main menu");
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1: {
                    int visitId = readPositiveInt("Enter Visit ID: ");
                    if (p.visitHistory.searchVisit(visitId) != null) {
                        System.out.println("A visit with ID " + visitId + " already exists for this patient.");
                        break;
                    }
                    String date = readRequiredString("Enter Visit Date (e.g. 2026-09-06): ");
                    String doctor = readRequiredString("Enter Doctor Name: ");
                    String diagnosis = readRequiredString("Enter Diagnosis: ");
                    String treatment = readRequiredString("Enter Treatment: ");
                    boolean added = p.visitHistory.addVisit(
                            new Visit(visitId, date, doctor, diagnosis, treatment));
                    System.out.println(added
                            ? "Visit added successfully."
                            : "A visit with ID " + visitId + " already exists for this patient.");
                    break;
                }
                case 2: {
                    int visitId = readPositiveInt("Enter Visit ID to remove: ");
                    Visit removed = p.visitHistory.removeVisit(visitId);
                    System.out.println(removed != null
                            ? "Removed ->\n" + removed
                            : "Visit ID not found.");
                    break;
                }
                case 3: {
                    int visitId = readPositiveInt("Enter Visit ID to search: ");
                    Visit v = p.visitHistory.searchVisit(visitId);
                    System.out.println(v != null ? "Found ->\n" + v : "Visit ID not found.");
                    break;
                }
                case 4:
                    System.out.println("--- Full Visit History (total: " + p.visitHistory.size() + ") ---");
                    p.visitHistory.display();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /* --------------------------- Input helpers --------------------------- */
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0)
                return value;
            System.out.println("Please enter a number greater than zero.");
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static String readRequiredString(String prompt) {
        while (true) {
            String value = readString(prompt);
            if (!value.isEmpty())
                return value;
            System.out.println("This field cannot be empty.");
        }
    }
}