---

# Task Management Project

## Overview

The Task Management Project is a robust and dynamic application designed to streamline task management processes for both administrators and users. This project offers a comprehensive CRUD (Create, Read, Update, Delete) functionality for managing tasks and users. Beyond basic operations, the application is enriched with advanced business logic, ensuring that all operations are performed under strict validation and exception handling. This ensures that the data remains consistent and reliable, even as the system grows and evolves.

## Features

### User Management
- **Create User:** Allows the creation of new users with roles predefined as either `ADMIN` or `USER`. By default, new users are assigned the role `USER`.Full name, email, and password are mandatory fields to ensure completeness.
- **Update User:** Users can update their information, including full name, email, and password. The `userId` remains immutable, ensuring data integrity.
- **Delete User:** Users can be deleted from the system, provided they have no active tasks. If a user has any assigned tasks, they cannot be deleted, ensuring task continuity and responsibility tracking.
- **Get User by ID:** Retrieve detailed information about a specific user using their `userId`.
- **List All Users:** Get a complete list of all users in the system.

### Task Management
- **Create Task:** Tasks can be created with a specific title, description, due date, and an optional status. If no status is provided, tasks default to `PENDING`. Tasks are associated with specific users based on their `userId`. If a user is specified during task creation, the system checks to ensure that the user exists.
- **Update Task:** Update the details of an existing task, including its title, description, due date, and status. Update the details of an existing task, including its title, description, due date, and status. If any part of entity is not provided during an update, it remains as it is.
- **Delete Task:** Tasks can be deleted by `taskId`. The system ensures that tasks are appropriately removed and associated users are updated accordingly.
- **Get Task by ID:** Retrieve detailed information about a specific task using its `taskId`.
- **List All Tasks:** Get a complete list of all tasks in the system,with the ability to filter by completion status (completed or non-completed tasks)
- **Assign Task to User:** Tasks can be assigned to specific users. This process ensures that the task is not already assigned to another user, maintaining task ownership integrity.
- **Update Task Status:** Users can update the status of their tasks, progressing them through different stages such as `PENDING`, `IN_PROGRESS`, and `COMPLETED`.

### Validation and Exception Handling
The project includes robust validation mechanisms and exception handling:
- **Custom Exceptions:** The system raises appropriate exceptions, such as `CustomNotFoundException`, to handle cases where users or tasks are not found.
- **Task Duplication Checks:** The system prevents the creation of duplicate tasks for the same user, ensuring task uniqueness.
- **Past Due Date Checks:** Tasks cannot be assigned a due date in the past, preventing unrealistic task deadlines.
- **Graceful Deletion:** Deleting users and tasks is handled gracefully, with appropriate error messages returned in case of issues.

### Lombok Integration
- **Lombok Annotations:**  Added Lombok annotations to reduce boilerplate code, improve readability, and simplify the management of entity classes.
- **Code Optimization:** Refactored code to leverage Lombok for generating getters, setters, and constructors, enhancing code maintainability and reducing manual coding effort.

## Project Structure

- **Entities:** `UserEntity`, `TaskEntity` – Define the core data structures and relationships.
- **Repositories:** `UserRepository`, `TaskRepository` – Provide the data access layer for users and tasks.
- **Services:** `UserService`, `TaskService` – Contain the business logic and operations.
- **Controllers:** `UserController`, `TaskController` – Handle HTTP requests and responses, interfacing with the service layer.
- **Exceptions:** Custom exception classes to manage error handling across the application.

## Future Enhancements

This project is actively maintained and open to new ideas. Potential future enhancements include:
- **Role-Based Access Control:** Further refinement of user roles and permissions.
- **Task Prioritization:** Adding the ability to prioritize tasks within a user’s task list.
- **Reporting and Analytics:** Generating reports and analytics based on task completion rates, user activity, etc.
- **Notifications:** Implementing a notification system to remind users of pending tasks or approaching due dates.

## Contribution

Contributions to this project are welcome! Whether it’s bug fixes, new features, or improvements to existing code, your input is valuable. Please submit your pull requests or reach out with your ideas.

---
