# Introduction #
The Salesforce Data Loader Command Line Interface (CLI) is a powerful tool for automating business processes, and integrating Salesforce with other systems.  However, configuring the CLI is daunting, especially for first-time users.  CLI quickstart (CLIq) is the solution!

# Important #
Go to this link to download the latest version:
https://github.com/theswamis/dataloadercliq/releases/tag/2.3.0

LATEST INSTALLATION INSTRUCTIONS
https://github.com/theswamis/dataloadercliq/wiki/Installation

### Please Provide Feedback! ###
I would love to know about your experience - good or bad!  All of your feedback helps me know what is important to enhance and fix.

If you had a great experience to share, please [Post a Review](http://developer.force.com/codeshare/apex/ProjectPage?id=a0630000005BBjnAAG) (Thank you!)

For general feedback about the application, please
[Give Feedback](https://spreadsheets.google.com/viewform?key=0AsSsHFCeHI22dE44T3FhOTlFX2dCSzduYWQxa1JNVFE)

Thank you!

### CLIq 2 Released ###
  * Supports all Data Loader versions - no need to download a separate version of CLIq for each version of Data Loader
  * Support for all Data Loader operations including Extract All and Hard Delete
  * Validation of external IDs for upsert
  * Uses Data Loader code for Proxy - reliability should be equal to Data Loader with proxy environments
  * Additional parameters in cliq.properties
  * Eliminated unnecessary libraries - new download is much smaller

### What Others are Saying ###
> "I just wanted to take a moment to let you know how much I appreciated CLIq.  Had no idea what to do... would have taken me forever to do manually what CLIq does in minutes. Thank you very much for writing the code!!!!" - Michael H.

> "Just wanted to say thanks. We are migrating to SF and I did spend several hours trying to get the data loader to extract and save a csv file unsuccessfully. Using your tool took 2 minutes." - Alan V.

> "I truly appreciate your effort in creating such an amzing, time savng tool." - Kunal M.

Had a great experience too?  You can leave a rating and comment on the Force.com Code share site for
[CLIq Reviews](http://developer.force.com/codeshare/apex/ProjectPage?id=a0630000005BBjnAAG)

### Take Action ###
  * [Download](https://github.com/theswamis/dataloadercliq/releases)
  * [Get Started](https://github.com/theswamis/dataloadercliq/wiki/Installation)
  * [Give Feedback](https://spreadsheets.google.com/viewform?key=0AsSsHFCeHI22dE44T3FhOTlFX2dCSzduYWQxa1JNVFE)

## Overview ##
**CLIq provides a _simple wizard_ to create directory structures and configuration files for the Salesforce Data Loader Command Line Interface.**  You can spend hours configuring the CLI manually, or use CLIq and you will have a working configuration in less than **5 minutes**.
After creating a CLIq configuration, you can reference the Data Loader documentation and make adjustments as needed.

<a href='http://www.youtube.com/watch?feature=player_embedded&v=myJtSy3XtIA' target='_blank'><img src='http://img.youtube.com/vi/myJtSy3XtIA/0.jpg' width='720' height=480 /></a>

## What CLIq Does ##
  * **Quickly configure scheduled exports/updates for your Salesforce Org**
  * Text and graphical interface
  * Handles all the tricky stuff:
    * Creates a process-conf.xml
    * Validates your username/password, queries, and entity names (avoids case-sensitivity issues)
    * Encrypts password
    * Creates an organized directory structure
    * Creates a bat file which can be scheduled on Windows using Windows Scheduler
    * Creates a sh script which can be scheduled on UNIX using cron
  * Written in Java - Tested on Windows and Mac OS X (UNIX)!

| **Start** | **Finish** |
|:----------|:-----------|
|![http://lh4.ggpht.com/_alGXTM7ympw/SyKSa5vOA5I/AAAAAAAAA9c/wVQNEE6RedQ/s400/operation.png](http://lh4.ggpht.com/_alGXTM7ympw/SyKSa5vOA5I/AAAAAAAAA9c/wVQNEE6RedQ/s400/operation.png)|![http://lh3.ggpht.com/_alGXTM7ympw/SyKo6-yRi7I/AAAAAAAAA9k/COLSrBMHITI/s400/result.png](http://lh3.ggpht.com/_alGXTM7ympw/SyKo6-yRi7I/AAAAAAAAA9k/COLSrBMHITI/s400/result.png)|


## What CLIq Doesn't Do (Yet) ##
  * Write SOQL statements (for Exports) - You'll need to know how to write SOQL.
  * Configure Data Loader to write directly to Databases
  * Automatically configure SDL file.


# Help #
**If you experience any problems using CLIq, please let me know! I'm glad to help, and every interaction helps make the product better!**
  * For questions about using or troubleshooting CLIq, please post your question to the [Google Group](http://groups.google.com/group/data-loader-cliq)
  * _**After using CLIq, please take a minute to give your feedback**_ with a [Quick Survey](https://spreadsheets.google.com/viewform?key=0AsSsHFCeHI22dE44T3FhOTlFX2dCSzduYWQxa1JNVFE)

## Support ##
This project is maintained and supported by the community.  Salesforce.com does not provide support for CLIq.

If you find a bug, please create an Issue.
If you had a great experience, please let me know!

If you want to reach me, my email is vijay.swamidass(@)gmail.com.  Please use the Google Group for technical questions about CLIq.  Thanks!
