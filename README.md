# Hospital Management System

A console-based Java hospital management system demonstrating four linked data structures:

- A binary search tree for patient records keyed by patient ID
- A linked FIFO queue for emergency patients
- A linked LIFO stack for completed treatment records
- A singly linked list for each patient's visit history

## Project Structure

```text
src/
	HospitalManagementSystem.java
```

The implementation keeps the model classes and linked data-structure nodes in one source file for this console assignment. `HospitalManagementSystem` owns the menus and coordinates the structures.

## Main Features

- Patient records: insert, search, delete, and ascending in-order display through a patient-ID BST
- Emergency care: enqueue registered patients, preview the next patient, display the FIFO queue, and dequeue the next patient for treatment
- Treatment history: record completed emergency treatments and push, pop, peek, or display records using a LIFO stack, including patient-specific lookup
- Visit history: add, remove, search, and display visits stored in a linked list owned by each patient, with unique visit IDs per patient
- Input handling: retry invalid numeric input, reject non-positive identifiers and ages, require text fields, and validate ISO dates
- System summary: view current patient, queue, and treatment counts from the main menu

## Compile

From the project root on Windows:

```text
javac -d out src\HospitalManagementSystem.java
```

## Run

```text
java -cp out HospitalManagementSystem
```

The application is menu-driven and stores data in memory for the current run. The `out/` directory contains generated `.class` files and is excluded from version control.

## Example Usage

1. Register a patient from **Patient Records**.
2. Add the patient to the **Emergency Patient Queue**.
3. Preview the next patient, then dequeue and record the completed treatment.
4. Use **Patient Visit History** to record and review visits for the patient.