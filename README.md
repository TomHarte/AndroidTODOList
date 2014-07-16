Android TODO List
=================

This is an application for maintaining a TODO list. It was extended from the project supplied to us more or less in copy and paste form during the two-hour CodePath session.

Time spent: 4 hours plus two-hour session

# Completed user stories

* [x] Required: add a task
* [x] Required: remove a task
* [x] Required: edit a task
* [x] Required: persistence of list between launched

# Notes

There are two simple activities, one adaptor and a couple of model files. The CodePath TODO tutorial strongly influenced the main activity, with the Rotten Tomatoes tutorial being a source of inspiration for the adaptor. The two model classes — a TODO item and a TODO list — were, like the edit activity, self-written hopefully to evidence my suitability for the eight-week Android course.

I factored out the list itself to a model object; it inherits directly from `SQLiteOpenHelper` which may not be ideal — it enshrines the idea there'll be only one of them. It's a singleton so that assumption has flowed deeper into the project. A custom adaptor acts both to surrender all list maintenance to the TODOList object and to substitute my custom cell layout.

# Add task walkthrough
![Add task Walkthrough](images/AddTask.gif)

# Remove task walkthrough
![Remove task Walkthrough](images/RemoveTask.gif)

# Edit task walkthrough
![Remove task Walkthrough](images/EditTask.gif)

# Persistence walkthrough
![Remove task Walkthrough](images/Persistence.gif)