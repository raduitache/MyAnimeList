# MyAnimeList

## Developer documentation

### Navigation

This project uses compose navigation, while leveraging the principles of dependency inversion in order to increase build times and encapsulate implementations. The structure follows the pattern of NavGraph -> GraphRoute -> NavRoute, where the NavGraph is a sub-navigation graph, the graph route is the entry point to the said navigation graph, and the navigation route is one or multiple internal routes. 

We have also introduced the concept of MainNavGraph, which is a navigation graph representing one of the navigation items available in the bottom navigation. Such a graph must implement a NavigationItem composable, which defines the icon/subhead/action on click, as well as a navigationItemIndex, to keep control over the order of the elements in the bottom bar.

The navigation route should define a Content composable, each taking parameters as it needs. This will be what the actual screen being rendered need in order to function properly, and of course, that screen should also have a ViewModel.

#### Responsibilities

Graph route - only responsible for the entry point to the Navigation Graph. Navigation between Navigation Routes should only happen in the same Navigation Graph.
Navigation Route - defines a navigation path, and associates a screen with it.
Screen - Just displays data on the screen.
ViewModel - Provides the screen with data to display and handles business logic. Very often that'll just mean calling the appropriate services and structuring the data for the screen to use.
Navigation Graph - its responsibility is defining navigation. It's the only element out of all the others mentioned above that should know about the Navigation Controller. If screens need to navigate from one another, the navigation is restricted and provided by the Navigation Graph, through lambdas. Such lambdas will be given as parameters to the Content composable of the Navigation Routes when the graph is defined.

#### An important note

Given the time limitations and the lifetime of the project, the navigation infrastructure is not as complete as desired. This results in a limitation: all the Navigation Routes of a Navigation Graph should have their routes start with the route of the Graph route.

For example: If the graph route is "Feature", it is mandatory that the navigation route is of the form "Feature/whatever".

#### Another important note

The separation between API and implementation has considerable implications, both from a build time point of view, as well as a testability point of view. APIs should only depend on other APIs, and maybe simple data classes, and implementations should, as well, only depend on APIs and simple data classes. As a result, the structure of any new feature should be the following

feature/ -> the definition of the APIs
feature/impl/ -> implementation of the said APIs
feature/impl/module -> binding between the API and the implementation. Whenever an API is injected, an implementation instance should be provided, and this is where we define which one.

### Authentication

In this application, authentication happens by going through an oAuth2 flow for the My Anime List API, getting the access and refresh tokens, requesting the User ID from MAL, and generating a custom Firebase authentication token based on that ID. 

This way, we can ensure the identification of different users, without duplicating data. The access and refresh tokens are then stored in a Firebase Realtime Database and associated to the user.

### Services

Most of the data comes from the MAL API, therefore services handle mostly HTTP requests. Given the context, Retrofit will be providing most of the services. 

The infrastructure currently supports injecting a Flow of retrofit instances, updated by the values of the access and refresh tokens stored in the Realtime Database, and that's because it's the only way to react to events regarding the authentication state: 
if the user is logged out, requests are authenticated differently than when the user is logged in. The provider also handles refreshing the tokens for a logged in user if they expire, and update the values in the Realtime Database. That will, as a result, update the data, and should update the headers any of the services use in requests.

This means that when creating a new Retrofit Service, their module should provide them as a flow of said service, mapped directly based on the flow of Retrofit instances. They should be used in the ViewModels in the same manner, to ensure no outdated states are being used.
