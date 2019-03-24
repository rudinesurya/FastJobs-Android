# Mobile App Development

Name: Rudine Surya Hartanto

## Overview

##### App Concept:
This is an app for quick contract job, similar to the likes of Meetup.
It has some form of social networking feature, eg. the ability to view user's profile and chat


##### Objectives:
+ For the host: Enable quick solution for finding adhoc work done.
+ For the jobseeker: Enable checking out the job details and payment before joining.
 

##### Features:
 + CRUD operation
 + List Jobs (Sorting, Filtering)
 + View & Edit Job Detail
 + Chat system in the job page
 + Login / Register
 + View other user's profile
 + Edit self user profile
 + Join / Leave Job


## Installation requirements


## UI Design


## Model View ViewModel(MVVM) with Clean Architecture

Architecture components help you structure your app in a way that is robust, testable, and maintainable with less boilerplate code.

This diagram shows a basic form of this architecture:

<img src="readme_img/mvvm_arch.png" width="400">


#### LiveData: A data holder class that can be observed. The UI that is observing the livedata will react when data changes.

#### ViewModel: Provides data to the UI. Acts as a communication center between the Repository and the UI. Hides where the data originates from the UI. ViewModel instances survive Activity/Fragment recreation.
<img src="readme_img/viewmodel_lifecycle.png" width="200">

#### Repository: A Repository class abstracts access to multiple data sources.

#### DAO: Data access object. Interface between Repository and the database.

#### Firestore: cloud persistence database which also supports caching for offline data.

#### Model: The model data class.


### Advantages of MVVM with Clean Architecture
+ MVVM separates your view (i.e. Activitys and Fragments) from your business logic.
+ Your code is further decoupled.
+ The package structure gets easier to navigate.
+ The project is easier to maintain.
+ Your team can add new features more quickly.




## Third party libraries

+ android arch
+ android jetpack
+ android ktx
+ kotlin coroutines
+ timber (for logging)
+ firebase
+ firebase storage
+ firestore
+ google-map
+ react-places-autocomplete
+ retrofit
+ glide
+ epoxy recyclerview
+ kodein
+ junit4
+ assertj


## References


