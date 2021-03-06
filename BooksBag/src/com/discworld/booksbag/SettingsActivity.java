package com.discworld.booksbag;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity
{
   @Override
   protected void onCreate(final Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
   }

   public static class MyPreferenceFragment extends PreferenceFragment
   {
      @Override
      public void onCreate(final Bundle savedInstanceState)
      {
         super.onCreate(savedInstanceState);
         addPreferencesFromResource(R.xml.preference_screen);
      }
   }
}
