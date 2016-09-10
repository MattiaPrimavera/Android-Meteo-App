# Sunshine Meteo App
Simple Meteo App by following Udacity tutorials on Android

In order to get up and running with Android Development, I chose to follow some [Android Video Lessons from
Udacity], which I found out being an interesting and enjoyable way of approaching Android development.

# Synopsis

A simple Meteo app has been chosen has an example of a common situation occurring in most of Android apps:
a main ListView which leads to a DetailedView whenever clicking on one of the items.
This is also known as ***"master-detail" flow***.

If the device’s screen size can accommodate showing both the list and the details, two fragments will
be used, all the information being visible at one time.
Otherwise, just one is used and the user must click to get deeper details of an element from the list4.

# Screenshots

Soon to come ...

# Architecture

!["Image Taken From Udacity Class"](http://lh3.ggpht.com/JnCPvS_BHwqRiKSKEei1cmbdfLsvO1m1ys59TLdG5kv2AdiooZ6Bm0jXRKd-K2hQCfRC6U8O_4EdnXxcxsw=s0#w=676&h=344)

# Subjects covered
Check [Here](https://www.udacity.com/course/developing-android-apps--ud853)

# Important Definitions
## Activity
> An activity is a single, focused thing that the user can do. Almost all activities interact with the user, so the Activity class takes care of creating a window for you in which you can place your UI with setContentView(View). While activities are often presented to the user as full-screen windows, they can also be used in other ways: as floating windows (via a theme with windowIsFloating set) or embedded inside of another activity (using ActivityGroup). There are two methods almost all subclasses of Activity will implement:

> - `onCreate(Bundle)` is where you initialize your activity. Most importantly, here you will usually call `setContentView(int)` with a layout resource defining your UI, and using `findViewById(int)` to retrieve the widgets in that UI that you need to interact with programmatically.
> - `onPause()` is where you deal with the user leaving your activity. Most importantly, any changes made by the user should at this point be committed (usually to the ContentProvider holding the data). 

> The entire lifecycle of an activity is defined by the following Activity methods. All of these are hooks that you can override to do appropriate work when the activity changes state. All activities will implement onCreate(Bundle) to do their initial setup; many will also implement onPause() to commit changes to data and otherwise prepare to stop interacting with the user. You should always call up to your superclass when implementing these methods.

```java
public class Activity extends ApplicationContext {
     protected void onCreate(Bundle savedInstanceState);
     protected void onStart();
     protected void onRestart();
     protected void onResume();
     protected void onPause();
     protected void onStop();
     protected void onDestroy();
 }
 ```

## Fragments
> A Fragment represents a behavior or a portion of user interface in an Activity. You can combine multiple fragments in a single activity to build a multi-pane UI and reuse a fragment in multiple activities. You can think of a fragment as a modular section of an activity, which has its own lifecycle, receives its own input events, and which you can add or remove while the activity is running (sort of like a "sub activity" that you can reuse in different activities).

> A fragment must always be embedded in an activity and the fragment's lifecycle is directly affected by the host activity's lifecycle. For example, when the activity is paused, so are all fragments in it, and when the activity is destroyed, so are all fragments. However, while an activity is running (it is in the resumed lifecycle state), you can manipulate each fragment independently, such as add or remove them.

Aims:
- support more dynamic and flexible UI designs on large screens, such as tablets.
- By dividing the layout of an activity into fragments, you become able to modify the activity's appearance at runtime and preserve those changes in a back stack that's managed by the activity.

> Design each fragment as a modular and reusable activity component

## Adapters
> An Adapter object acts as a bridge between an `AdapterView` and the underlying data for that view. The Adapter provides access to the data items. The Adapter is also responsible for making a `View` for each item in the data set.
- `ArrayAdapter`: it's an adapter backed by an array of objects
- `SimpleCursorAdapter`: the cursor is a set of data you usually get when you do a database query. The result of your query is contained in the cursor. This adapter binds the Cursor data to an Adapter View. You define a layout that controls how each row of data is displayed

It provides you with an efficient pagination system for displayng list results to the screen, by reusing those views which are not visible anymore when scrolling, otherwise the garbage collector would be charged by all those unused references. 

## ListView
> `ListView` is a view group that displays a list of scrollable items. The list items are automatically inserted to the list using an `Adapter` that pulls content from a source such as an array or database query and converts each item result into a `view` that's placed into the list.

> Using a `CursorLoader` is the standard way to query a `Cursor` as an `asynchronous task` in order to avoid blocking your app's main thread with the query. When the `CursorLoader` receives the `Cursor` result, the `LoaderCallbacks` receives a callback to `onLoadFinished()`, which is where you update your `Adapter` with the new Cursor and the list view then displays the results.

## Loaders
> Introduced in Android 3.0, loaders make it easy to asynchronously load data in an activity or fragment. Loaders have these characteristics:
> - They are available to every Activity and Fragment.
> - They provide asynchronous loading of data.
> - They monitor the source of their data and deliver new results when the content changes.
> - They automatically reconnect to the last loader's cursor when being recreated after a configuration change. Thus,
> - they don't need to re-query their data.

Using Loaders in an Application

> An application that uses loaders typically includes the following:

> - An Activity or Fragment.
> - An instance of the LoaderManager.
A CursorLoader to load data backed by a ContentProvider. Alternatively, you can implement your own subclass of Loader or AsyncTaskLoader to load data from some other source.
> - An implementation for LoaderManager.LoaderCallbacks. This is where you create new loaders and manage your references to existing loaders.
> - A way of displaying the loader's data, such as a SimpleCursorAdapter.
> - A data source, such as a ContentProvider, when using a CursorLoader.


