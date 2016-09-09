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
