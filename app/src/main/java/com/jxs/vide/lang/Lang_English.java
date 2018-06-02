package com.jxs.vide.lang;

import com.jxs.vide.*;

public class Lang_English extends Lang {
	@Override
	public String getName() {
		return "English";
	}
	@Override
	protected void init() {
		qwe(L.Importing, "Importing...");
		qwe(L.Wrong, "There is something wrong");
		qwe(L.Done, "Done!");
		qwe(L.DaShang, "Welcome Support!");
		qwe(L.HereIsEmpty, "Nothing here");
		qwe(L.Editor_Run, "Run");
		qwe(L.Editor_Output, "Build");
		qwe(L.Editor_Setting, "Setting");
		qwe(L.Editor_Import, "Import");
		qwe(L.Remind, "Remind");
		qwe(L.Editor_DidntSave, "You didn't save yet, are you sure to exit?");
		qwe(L.Editor_SaveAndExit, "Save & Exit");
		qwe(L.Editor_DirectlyExit, "Exit");
		qwe(L.Editor_Cancel, "Cancel");
		qwe(L.Wait, "Wait a moment");
		qwe(L.Editor_Reading, "Reading...");
		qwe(L.Editor_ErrorWhileReading, "An error happened while reading");
		qwe(L.Editor_File, "File");
		qwe(L.Editor_More, "More");
		qwe(L.Editor_Name, "Name");
		qwe(L.NameCantEmpty, "Name can not be empty");
		qwe(L.ContainsIllegalChar, "Contains illegal char");
		qwe(L.Editor_ConfigEdited, "Edited successfully!");
		qwe(L.Editor_OK, "OK");
		qwe(L.Editor_FileAlreadyExist, "File already exists");
		qwe(L.Editor_CreateFile, "Create File");
		qwe(L.Editor_OpenFile, "Open");
		qwe(L.Editor_SaveFile, "Save");
		qwe(L.Editor_FileName, "File Name");
		qwe(L.Editor_Added, "Added successfully!");
		qwe(L.Editor_Close, "Close");
		qwe(L.Editor_Saved, "Saved ssuccessfully!");
		qwe(L.Editor_ErrorWhileSaving, "An error happened while saving");
		qwe(L.Editor_RunWithoutSave, "You didn't save yet, are you sure to run?");
		qwe(L.Editor_SaveAndRun, "Save & Run");
		qwe(L.Editor_DirectlyRun, "Run");
		qwe(L.Editor_OutputWithoutSave, "You didn't save yet, are you sure to build apk?");
		qwe(L.Editor_SaveAndOutput, "Save & Build");
		qwe(L.Editor_DirectlyOutput, "Build");
		qwe(L.Editor_ChooseOutputDir, "Select a dir to save apk");
		qwe(L.Editor_Encrypting, "Encrypting...");
		qwe(L.Editor_PickingOutApk, "Coping apk...");
		qwe(L.Editor_ExportingAM, "Extract AndroidManifest.xml...");
		qwe(L.Editor_ParsingAM, "Parsing AndroidManifest.xml...");
		qwe(L.Editor_EditingPackageName, "Editing package name...");
		qwe(L.Editor_ErrorAM, "AndroidManifest.xml Error");
		qwe(L.Editor_CompressingApk, "Compressing Apk...");
		qwe(L.Editor_Signing, "Signing...");
		qwe(L.Editor_BuildedApk, "Done,the process takes %d millisecond");
		qwe(L.Editor_CanNotChooseFile, "Can not choose a file");
		qwe(L.Editor_PackageNameStartsWithDigit, "Package name can not start width digit");
		qwe(L.Editor_PackageNameContainsIllegalChar, "Package name contains illegal chars");
		qwe(L.Editor_BuildingApk, "Building apk");
		qwe(L.Main_Crash, "Crash Error");
		qwe(L.Main_About, "About");
		qwe(L.Main_CreateProject, "Create");
		qwe(L.OK, "OK");
		qwe(L.Main_ProjectAlreadyExist, "Project already exists");
		qwe(L.Main_ProjectCreated, "Created Successfully");
		qwe(L.NoneParameter, "No Parameters");
		qwe(L.CouldNotLoadIcon, "Can not load icon...");
		qwe(L.AppName, "App Name：");
		qwe(L.PackageName, "Package Name：");
		qwe(L.Main_AppName, "App Name");
		qwe(L.Main_PackageName, "Package Name");
		qwe(L.PackageNameCantEmpty, "Package name can not be empty");
		qwe(L.EnterEditor, "Edit");
		qwe(L.SaveManifest, "Save Settings");
		qwe(L.ManifestSaved, "Settings saved successfully");
		qwe(L.Project_UseCompat, "Use Compat");
		qwe(L.Description_Compat, "Use Compat can let your app seems similar on different Android device, and also avoid some crash, but it will make your apk bigger(about 1MB)");
		qwe(L.Project_UseDx, "Use Dx");
		qwe(L.Description_UseDx, "If you use dx, you can extend some class use function JavaAdapter, but it will also make your apk bigger(about 600KB)");
		qwe(L.Delete, "Delete");
		qwe(L.Warning, "Warning");
		qwe(L.SureDeleteProject, "Are you sure to delete this project?");
		qwe(L.Cancel, "Cancel");
		qwe(L.Deleted, "Deleted Successfully");
		qwe(L.IKnow, "OK");
		qwe(L.Settings, "Settings");
		qwe(L.Setting_GUI, "UI");
		qwe(L.Setting_SplashTime, "Splash Time");
		qwe(L.Setting_SplashTime_Des, "The time of VIDE stay at the splash UI(millisecond)");
		qwe(L.Setting_SplashTime_Hint, "Time (Millisecond)");
		qwe(L.IllegalNumber, "Illegal number");
		qwe(L.SplashTimeCantNegative, "Time can not be a negative number");
		qwe(L.Setted, "Setted successfully");
		qwe(L.Setting_ThemeColor, "Theme Color");
		qwe(L.Setting_ThemeColor_Des, "Set the theme color of app");
		qwe(L.Jar_Files, "Libs");
		qwe(L.Import_Jar, "Import Jar/Dex");
		qwe(L.Jar_Rename, "Rename");
		qwe(L.Jar_Delete, "Delete");
		qwe(L.Jar_Name, "Name of the lib");
		qwe(L.Jar_Name_Empty, "The name of the lib can not be empty");
		qwe(L.Jar_Renamed, "Renamed successfully");
		qwe(L.Jar_Deleted, "Lib deleted successfully");
		qwe(L.CanNotChooseDir, "Can not choose a dir!");
		qwe(L.Error, "Error");
		qwe(L.CanOnlyChooseJarOrDex, "You can only choose jar or dex file");
		qwe(L.Jar_Imported, "Imported successfully");
		qwe(L.Converting, "Converting");
		qwe(L.Converted, "Converted successfully");
		qwe(L.Editor_MergingDex, "Merging dex...");
		qwe(L.Learn, "Learn");
		qwe(L.Run, "Run");
		qwe(L.ShowVButton, "Show VButton");
		qwe(L.ShowVButton_Subtitle, "Show VButton as a floating window, you can see some log or control other things through it");
		qwe(L.PlzEnableOverlay, "Please give VIDE the permission to draw overlay");
		qwe(L.NoProject, "No project here.\nCreate one now!");
		qwe(L.ConsoleTitle, "Console");
		qwe(L.ThreadDialogTitle, "Thread Info");
		qwe(L.ThreadInfo, "Thread Name:%s\nThread ID：%d\nThread Priority%d\nThread Stack：\n%s");
		qwe(L.Last, "Upper");
		qwe(L.Debug, "Debug");
		qwe(L.Debugger_Evaluate, "Evaluate JS");
		qwe(L.Setting_Extra, "Extra");
		qwe(L.Setting_Language, "Language");
		qwe(L.Setting_Language_Des, "Set the language which VIDE use");
		qwe(L.Title_About, "About");
		qwe(L.Title_Edit, "Edit");
		qwe(L.Title_Learn, "Study");
		qwe(L.Title_Setting, "Setting");
		qwe(L.Debugger_EvalCode, "Execute");
		qwe(L.Debugger_NotInCurrentThread, "This JsProgarm did not run in the current thread");
		qwe(L.Debugger_CompileErr, "Compile error");
		qwe(L.Debugger_ExecErr, "Runtime error");
		qwe(L.VButton_Ticker, "VButton is showing now!");
		qwe(L.VButton_Title, "VButton is showing");
		qwe(L.VButton_Text, "Click me to dismiss VButton");
		qwe(L.Debugger_Variable, "Check Variables");
		qwe(L.Debugger_ScriptableInfo, "Name:%s\nType:%s\nValue:%s");
		qwe(L.UnknownError, "Unknown Error");
		qwe(L.Exit, "Quit");
		qwe(L.Title_VApp, "VApp");
		qwe(L.HaveFun, "Find the joys of \"Light\"");
		qwe(L.Login, "Login");
		qwe(L.Regist, "Regist");
		qwe(L.UserName, "User name");
		qwe(L.Password, "Password");
		qwe(L.Login_OR, "OR");
		qwe(L.UserNameCannotEmpty, "User name can not be empty");
		qwe(L.PasswordCannotEmpty, "Password can not be empty");
		qwe(L.UserNameExceed, "The length of your user name can not be more than %d");
		qwe(L.PasswordExceed, "The length of your password can not be more than %d");
		qwe(L.Registing, "Registing...");
		qwe(L.NoNetwork, "No network connection");
		qwe(L.ConnectTimeout, "Connection timed out");
		qwe(L.UserNameBeenUsed, "The username has been used");
		qwe(L.InvalidOldPassword, "Invalid old password");
		qwe(L.UploadFailed, "Upload failed");
		qwe(L.RegistSuccess, "Registed successfully");
		qwe(L.Logining, "Logining...");
		qwe(L.InvalidUserNameOrPassword, "Invalid user name or password!");
		qwe(L.Logined, "Logined successfully");
		qwe(L.Title_UploadVApp, "Upload VApp");
		qwe(L.Description, "Description");
		qwe(L.DesCannotBeEmpty, "Description can not be empty!");
		qwe(L.DesExceed, "The length of description can not be more than %d");
		qwe(L.DidnotChoosed, "You did not choose a project!");
		qwe(L.Uploading, "Uploading...");
		qwe(L.Compiling, "Compiling...");
		qwe(L.Uploaded, "Uploaded successfully!");
		qwe(L.Downloading, "Downloading...(%d%%)");
		qwe(L.MoreSettings, "More Settings");
		qwe(L.VersionInfo, "Version Info");
		qwe(L.EditPermissions, "Edit Permissions");
		qwe(L.VersionCode, "Version Code");
		qwe(L.VersionName, "Version Name");
		qwe(L.VersionCodeCannotBeEmpty, "Version code cannot be empty");
		qwe(L.VersionNameCannotBeEmpty, "Version name cannot be empty");
		qwe(L.EditSuccessfully, "Edit successfully!");
		qwe(L.OpenSource, "Open Source");
		qwe(L.UpdateDes, "VIDE - V%s");
		qwe(L.Update, "Update");
		qwe(L.PkgLessThanTwo, "Package name can not less than two parts");
		qwe(L.LoginFirst, "Please login first!");
		qwe(L.Please, "Please!");
		qwe(L.ShareApp, "Please help me!!! I'm so poor that I can not even buy a chicken!\nIf you think VIDE is great, you can either share it to your friend or rate it in app market!");
		qwe(L.Nop, "No way!");
		qwe(L.Share, "Share");
		qwe(L.AppMarket, "Rate");
		qwe(L.Badguy, "You a badguy!");
		qwe(L.IRepented, "I Repented");
		qwe(L.ShareContent, "Check out this App! VIDE allow you to develop App or Console Program on the phone——if you can't develop, the interesting small programs here may attract you too! Download Now -> https://www.coolapk.com/apk/com.jxs.vide");
		qwe(L.ShareVIDE, "Share VIDE");
		qwe(L.Nothing, "(Click me!)");
		qwe(L.EnableVIDELog, "Allow VIDE to print log");
		qwe(L.VAppRun, "Run");
		qwe(L.VAppClone, "Clone");
		qwe(L.Cloned, "Cloned to \"%s\"");
		qwe(L.VAppDetail, "Detail");
		qwe(L.VAppDelete, "Delete");
		qwe(L.Deleting, "Deleting...");
		qwe(L.OutputApk, "Apk");
		qwe(L.OutputJsc, "Jsc");
		qwe(L.Outputing, "Outputing...");
		qwe(L.OutputSC, "Output Successfuly!");
		qwe(L.ChoosePkg, "Choose Current Package");
		qwe(L.ChooseDir, "Choose Current Folder");
	}
	private void qwe(int l, String s) {
		set(l, s);
	}
}
