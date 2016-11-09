1. copy the package chromeCustomTabs and copy the animations to anim folder.

2. add the below gradle with your latest version
    compile 'com.android.support:customtabs:23.2.0'

3. This library requires API 15. To support it on minimum use
    <uses-sdk tools:overrideLibrary="android.support.customtabs" />

4. use the below code to open the url
   CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
   intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
   intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
   intentBuilder.setShowTitle(true);

   // set start and exit animations
   intentBuilder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
   intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);

   // build custom tabs intent
   CustomTabsIntent customTabsIntent = intentBuilder.build();

   // launch the url
   CustomTabActivityHelper.openCustomTab(this, customTabsIntent, Uri.parse(url), new CustomTabActivityHelper.CustomTabFallback() {
       @Override
       public void openUri(Activity activity, Uri uri) {
        // TODO when no custom tabs are available
        // open the url in other browser
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);

        //or open it in your own web view activity.
       }
   });


 ****************Advance********************
 If you want to open the url with warming up or pre-fetching, then

 1. in your activity onCreate
    CustomTabActivityHelper mCustomTabActivityHelper = new CustomTabActivityHelper();

 2. onStart():
    mCustomTabActivityHelper.bindCustomTabsService(this);

 3. onStop();
    mCustomTabActivityHelper.unbindCustomTabsService(this);