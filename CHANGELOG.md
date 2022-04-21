# v0.32.0(Apr 21 2022)
- ## **_Changes_**
  - Days that are **_next to each other_** are now **_Connected!_**
  - **_Pie Charts_** now use a **_new Library_**
  - **_Longest Streak_** is now in the **_Statistics!_**

- ## **_Bug Fixes_**
  - Code **_Cleanup_**
  - Fixed an issue where **_percentage lables_** weren't adding up to **_100%_**
  - Fixed an issue where if there **_weren't days marked until today_** application crashed
  - Fixed an issue on small screens **_Habit name in the Habit card_** could overlap **_Habit repeat text_**

# v0.31.1(Apr 14 2022)
- ## **_Changes_**
  - Added the option for **_Instant_** decoration speed

- ## **_Bug Fixes_**
  - Fixed an issue where when starting the main activity the theme **_opposite of the device default_** flashed

# v0.31.0(Apr 13 2022)
- ## **_Changes_**
  - **_Quotes & Counter_** Fragments are in the Navigation Drawer (Not Implemented Yet)
  - When **_All Days_** are Selected to **_Repeat Every day_** it says **_Every day_**
  - Indication that shows if Habits are **_Done or Failed_** is redesigned
  - "Show Month Calendar" & "Show All Habits" in different fragments than Home are hidden
  - **_Ability To Switch_** amount of **_Month Loads Per Swipe!_**
  - **_Ability To Switch_** load times of **_Decorated Days_**

- ## **_Bug Fixes_**
  - Loading a lot of Dates is now **_Optimized_** by loading only a **_Selected Amount_** of Days per **_Swipe and Load_**
  - Fixed an issue where some fragments **_Weren't Highlighted_** in the **_Navigation Drawer_**
  - Fixed an issue where on small devices **_Setting Text_** could have **_Overlapped_** on/off Switches in **_Setting Activity_**

# v0.3.0(Mar 12 2022)
   - ## **_Changes_**
     - **_Statistics_** are now Available!

  - ## **_Bug Fixes_**
     - Adding a lot of Dates is now **_Optimized_**

# v0.2.1(Mar 11 2022)
   - ## **_Changes_**
     - **_Selected Days_** are now **_Shown_** in the **_Action Bar_**
     - Now **_Able to Fail Days_** by Holding down the **_Date_** in the **_Calendar Fragment_**
     - If there are not any **_Habits_** in the **_Currently_** selected day, it **_Shows_** a text that there aren't any habits

  - ## **_Bug Fixes_**
     - When in a different fragment, clicking **_Show All Habits_** or **_Show Month Mode_** caused the application to **_Crash_**
     - **_Settings_** is removed from the **_Navigation Drawer_**
     - **_First Day Of Week_** didn't change in the **_Calendar Fragment_**
     - **_Habits_** now scale **_According to Your Device_**

# v0.2.0 (Mar 7 2022)
   - ## **_Changes_**
     - **_Habit Repeat_** is now **_Functional_**! Meaning that when you click on a day it will only show you the correct habits
     - **_Now Able_** to show all **_Habits_**
     - **_Switching_** between month and week mode **_has_** been added to the **_Action Bar_**

  - ## **_Bug Fixes_**
     - Code **_Optimization_**
     - The **_Floating Action Button_** now isn't visible in the **_Calendar Fragment_**

# v0.1.1 (Mar 6 2022)
   - ## **_Changes_**
     - **_UI Revamp_** is now also in **_Add Activity_** and **_Update Activtity_**
     - New **_Logo_**
     - Application will be called **_Repeaty_**
     - Menu Items are **_Always_** Visible
     - The calendar is now also functional in the **_Gallery Fragment_**
     - Buttons to **_Add_** or **_Remove_** a habit are now in the **_Action Bar_**
     - To add repetition to a habit there are **_Buttons_** that **_Add_** it for **_You_**

  - ## **_Bug Fixes_**
     - Code **_Optimization_**
     - When **_Entering_** the main Activity the Weekday Labels on the Calendar **_disappeared_**
     - **_Selecting_** or **_Deselecting_** should now be more Optimized

# v0.1.0 (Mar 2 2022)
   - ## **_Changes_**
     - **_Full UI_** Revamp
     - Animated **_Restarting and Entering_** Activities

  - ## **_Bug Fixes_**
     - Calendar clicks are **_Registered and Saved_** correctly without restarting the activity
     - Weekday labels **_Disappeared_** when reloading Activities
     - When fragments were switched the **_Main_** Fragment had **_Nothing_** in it
     - Pressing the **_Back Button_** now reloads the Activity
     - Now you **_Don't_** have to Click the Calendar Title twice to **_Open_** the **_Month View_**
