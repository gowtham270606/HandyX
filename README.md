# HandyX - Work Management App

HandyX is a simple tool built with Java to help teams manage their work. It's a console app, so it runs directly in your terminal. It helps managers organize things and employees keep track of their tasks.

## What can you do with it?

### If you're a Manager:
* **Manage your team:** Add new employees and keep their details updated.
* **Organize work:** Create projects and break them down into tasks for your team.
* **Handle leaves:** Check leave requests and approve or deny them.
* **Performance:** Give ratings and feedback to help your employees grow.
* **Shifts:** Set up work schedules and shifts.
* **Updates:** Post announcements for everyone to see.

### If you're an Employee:
* **Check your work:** See which projects and tasks are assigned to you.
* **Update progress:** Change your task status so everyone knows where things stand.
* **Apply for leave:** Send leave requests to your manager.
* **Stay informed:** Check company announcements and see your work shifts.
* **Get feedback:** View your performance reviews and ratings.

## How to run the project

### Using an IDE (like IntelliJ or Eclipse)
1. Open the project in your favorite IDE.
2. Go to `HandyX_V1_0_0/src/com/handyx/Main.java`.
3. Run the file.

### Using the Terminal
If you prefer the command line, run these commands from the project root:

```bash
# Compile the code
javac -d out -sourcepath HandyX_V1_0_0/src HandyX_V1_0_0/src/com/handyx/Main.java

# Run the app
java -cp out com.handyx.Main
```

## A few tips
* **Roles:** You can choose to be a Manager or an Employee when you sign up.
* **Dates:** When entering dates, use the format `dd-MM-yyyy` (like 26-05-2026).
* **Login:** The app starts with a landing menu where you can Sign In or Sign Up.
