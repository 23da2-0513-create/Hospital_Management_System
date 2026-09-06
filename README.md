# Hospital Management System

A console-based Java hospital management system demonstrating four linked data structures:

- A binary search tree for patient records keyed by patient ID
- A linked FIFO queue for emergency patients
- A linked LIFO stack for completed treatment records
- A singly linked list for each patient's visit history

## Compile

From the project root:

```text
javac -d out src\HospitalManagementSystem.java
```

## Run

```text
java -cp out HospitalManagementSystem
```

The application provides menu-driven operations for registering, searching, displaying, and deleting patients; managing the emergency queue; recording completed treatments; and maintaining visit histories.