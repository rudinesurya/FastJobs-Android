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
 + CRUD operation with cloud persistence (Firestore)
 + List Jobs (Sorting, Filtering)
 + Dynamic map view with gps (view nearby jobs and also places of interests)
 + View & Edit Job Detail
 + Chat system in the job page
 + Push notification sent to participants (when job cancelled/resumed)
 + Login / Register
 + View other user's profile
 + Edit self user profile
 + Join / Leave Job


## Installation requirements

Must provide your own google-services.json with valid api key

## UI Design

Clean Design Concept
+ Use action bar and navigation drawer to place menu items instead of cluttering them on the activity/fragment.
+ Sensible navigation and deep linking.
+ For more important features, allow multiple way to access it besides a button. (nav drawer)
+ User can sign out from anywhere in the app. (nav drawer)
+ Async loading enables seamless and smooth experience for the user.
+ Show loading bars when possible.

Adherence to the Android Material Design
+ CoordinatorLayout extends the ability to accomplish many of the Google's Material Design scrolling effects.
+ parallax scrolling effects animations for the action bars during scrolling effect.
+ expanding and collapsing action bar allows more screen real estate for displaying more important stuffs.


## Integration with Android ecosystem

+ Static maps can launch an intent to open the user's Google Maps.
+ Clicking on the job date will prompt user if he/she wants to add the job detail to the Google Calendar.
+ Other usage of intent filter for sharing job to (gmail,facebook,whatsap, other social media)


## Third party api

+ Google Maps
+ Google Places
+ Google Autocomplete
+ GPS
+ Firebase Authentication
+ Firebase cloud function


Google Maps, GPS, together with Google Places API are used for making REST requests with retrofit to get a list of nearby places of interest to display on the dynamic map view activity. 

Firebase cloud function is used as event trigger for when a job gets cancelled or resumed by host. It will then notify all participants involved. 

```
exports.notifyJobCancelled = functions.firestore
  .document('jobs/{id}')
  .onUpdate((change, context) => {
    const after = change.after.data();
    const jobId = context.params.id;
    
    // Creates the notification payload
    admin.firestore()
      .collection('jobs')
      .doc(jobId)
      .collection('participants')
      .get()
      .then(snapshot => {
        const notificationBody = after.status ? 'Host resumed the job' : 'Host cancelled the job';
        const payload = {
          notification: {
            title: 'FastJobs alert',
            body: notificationBody,
            clickAction: 'JobDetailActivity',
          },
        };

        // Send this notification to every registered tokens
        snapshot.forEach(user => {
          console.log(`userId ${user.id}`)
          admin.firestore()
            .collection('users')
            .doc(user.id)
            .get()
            .then(user => {
              const token = user.data().registerationToken
              admin.messaging().sendToDevice(token, payload);
            })
            .catch(err => console.log(err));
        });
      })
      .catch(err => console.log(err));
  });
```


## Complex Model Schema
User entity holds its own references to jobs that he/she has joined. This enables fast querying in the job dashboard, when we want to filter jobs that the current user has joined or has saved as favourite.
<img src="readme_img/user_model.png" width="400">

Job entity creates sub-collection to hold users that have joined and another collection for holding all the comments. This strategy can further be improved for large data as we can query sub-collection by pages. The advantage of not storing all these references in the job entity itself is that we dont bloat up the job entity size by irrelevant data. We also avoid hitting the size limit per document.
<img src="readme_img/job_model.png" width="400">

#### Further thoughts for improvement:
At the current state, we are storing duplicate data when we are referencing. We can improve this further by either storing user doc reference (Not sure if it might double the query), or by having a cloud function which periodically aggregate the data to sync.


## Unit Testing and Instrumentation Testing

UI Layer
+ Testing of ...

Model Layer
+ Testing of Model data classes

Database Layer
+ Testing of Data Access Object (DAO)
+ Testing of network rest api services


## Model View ViewModel(MVVM) with Clean Architecture

Architecture components help you structure your app in a way that is robust, testable, and maintainable with less boilerplate code.

This diagram shows a basic form of this architecture:

<img src="readme_img/mvvm_arch.png" width="400">

#### View: 
The view role in this pattern is to observe (or subscribe to) a ViewModel observable to get data in order to update UI elements accordingly.

#### ViewModel: 
ViewModel interacts with model and also prepares observable(s) that can be observed by a View. Acts as a communication center between the Repository and the UI. Hides where the data originates from the UI. ViewModel instances survive Activity/Fragment recreation.
<img src="readme_img/viewmodel_lifecycle.png" width="400">

#### Model: 
Model represents the data and business logic of the app. One of the recommended implementation strategies of this layer, is to expose its data through observables to be decoupled completely from ViewModel or any other observer/consumer.

The following diagram shows MVVM components and basic interactions.
<img src="readme_img/observer_pattern.png" width="400">

#### LiveData: 
A data holder class that can be observed. The UI that is observing the livedata will react when data changes. This allows the components in your app to be able to observe LiveData objects for changes without creating explicit and rigid dependency paths between them. LiveData respects Android Lifecycle and will not invoke its observer callback unless the LiveData host (activity or fragment) is in an active state. Adding to this, LiveData will also automatically remove the observer when the its host receives onDestroy().


#### Repository: 
A Repository class abstracts access to multiple data sources.

#### DAO: 
Data access object. Interface between Repository and the database.

#### Firestore: 
cloud persistence database which also supports caching for offline data.

#### Remote Data Source: 
For making REST API requests to the web.




### Advantages of MVVM with Clean Architecture
+ MVVM separates your view (i.e. Activities and Fragments) from your business logic.
+ Your code is further decoupled.
+ The package structure gets easier to navigate.
+ The project is easier to maintain.
+ Your team can add new features more quickly.


### Optimizations 
+ Using best practices for relationship database design
+ Usage of sub-collection within documents to optimize read
+ Usage of cross referencing with key ids to avoids multiple querying for specific results
+ Having a special class to cache common documents.

The store class is created to reduce calls to firestore for fetching the same data multiple times due to many screens sharing the same data. We reduced the number of calls from one call per activity/fragment to a single call throughout the whole app. There is no reason to constantly request for data as we are using listener and will receive live update from the database.
```
/***
 * Class used to hold references to common objects to avoid multiple querying of the same data
 */
class Store(private val repository: MyRepository) {
    // LiveData
    private val _jobs = MutableLiveData<List<Job>>()
    val jobs: LiveData<List<Job>?>
        get() = _jobs

    ...

    init {
        fetchAllJobs()
    }

    fun fetchAllJobs() {
        Timber.d("fetching All Jobs")
        repository.getAllJobsLiveData {
            it.observeForever {
                _jobs.postValue(it.data)
            }
        }
    }
}
```


### Dependency Injection with Kotlin
Dependency injection is an instrumental technique, used to decouple dependencies from the code.

#### Make our application KodeinAware
To provide dependencies to other classes, we need to supply kodein all the dependencies, usually done in the Application class as there will only be one instance of that class and it does not have a lifecycle. 
```
class MyApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@MyApplication))

        bind() from singleton { FirebaseFirestore.getInstance() }
        bind() from singleton { JobDao(instance()) }
        bind() from singleton { MyRepository(instance(), instance(), instance(), instance()) }
        bind() from provider { ViewModelFactory(instance(), instance(), instance(), instance()) }
        ...
    }
}
```

To enable the dependencies to be passed, we need our classes to implement KodeinAware interface. This gives us an advantage of maintaining clean code and reduce lots of boilerplate due to passing references between classes.
```
class JobListFragment : KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance() // Get access to the view model factory
    private val auth: Auth by instance() // Get access to the auth class

    ...

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(JobListViewModel::class.java)

        ...
    }
}
```


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
+ places-autocomplete
+ retrofit
+ glide
+ date-time-picker
+ epoxy recyclerview
+ kodein
+ junit4
+ espresso
+ mockk
+ assertj


## References


