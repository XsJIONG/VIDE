package com.jxs.vide;
import java.lang.reflect.Field;
import java.util.HashMap;

public class L {
	public static int Importing,Wrong,Done,DaShang,HereIsEmpty,
	Editor_Run,Editor_Output,Editor_Setting,Editor_Import,
	Remind,Editor_DidntSave,Editor_SaveAndExit,Editor_DirectlyExit,Editor_Cancel,
	Wait,Editor_Reading,Editor_ErrorWhileReading,Editor_File,Editor_More,Editor_Name,
	NameCantEmpty,ContainsIllegalChar,Editor_ConfigEdited,Editor_OK,Editor_FileAlreadyExist,
	Editor_CreateFile,Editor_OpenFile,Editor_SaveFile,Editor_FileName,Editor_Added,Editor_Close,Editor_Saved,Editor_ErrorWhileSaving,
	Editor_RunWithoutSave,Editor_SaveAndRun,Editor_DirectlyRun,
	Editor_OutputWithoutSave,Editor_SaveAndOutput,Editor_DirectlyOutput,Editor_ChooseOutputDir,Editor_Encrypting,Editor_PickingOutApk,
	Editor_ExportingAM,Editor_ParsingAM,Editor_EditingPackageName,Editor_ErrorAM,Editor_CompressingApk,Editor_Signing,Editor_BuildedApk,
	Editor_CanNotChooseFile,Editor_PackageNameStartsWithDigit,Editor_PackageNameContainsIllegalChar,Editor_BuildingApk,
	Main_Crash,Main_About,Main_CreateProject,OK,Main_ProjectAlreadyExist,Main_ProjectCreated,
	NoneParameter,CouldNotLoadIcon,AppName,PackageName,Main_AppName,Main_PackageName,PackageNameCantEmpty,EnterEditor,
	SaveManifest,ManifestSaved,Project_UseCompat,Description_Compat,Project_UseDx,Description_UseDx,Delete,Warning,SureDeleteProject,Cancel,Deleted,
	IKnow,Settings,Setting_GUI,Setting_SplashTime,Setting_SplashTime_Des,Setting_SplashTime_Hint,IllegalNumber,SplashTimeCantNegative,Setted,Setting_ThemeColor,
	Setting_ThemeColor_Des,Jar_Files,Import_Jar,Jar_Rename,Jar_Delete,Jar_Name,Jar_Name_Empty,Jar_Renamed,Jar_Deleted,CanNotChooseDir,
	Error,CanOnlyChooseJarOrDex,Jar_Imported,Converting,Converted,Editor_MergingDex,Learn,Run,RunResult,ShowVButton,ShowVButton_Subtitle,PlzEnableOverlay,
	NoProject,ConsoleTitle,ThreadDialogTitle,ThreadInfo,Last,Debug,Debugger_Evaluate,Setting_Extra,Setting_Language,Setting_Language_Des,Title_Learn,Title_Setting,Title_About,Title_Edit,
	Debugger_EvalCode,Debugger_NotInCurrentThread,Debugger_CompileErr,Debugger_ExecErr,VButton_Ticker,VButton_Title,VButton_Text,Debugger_Variable,Debugger_ScriptableInfo,
	UnknownError,Exit,Title_VApp,HaveFun,Login,Regist,UserName,Password,Login_OR,UserNameCannotEmpty,PasswordCannotEmpty,UserNameExceed,PasswordExceed,Registing,NoNetwork,ConnectTimeout,
	UserNameBeenUsed,InvalidOldPassword,UploadFailed,RegistSuccess,Logining,InvalidUserNameOrPassword,Logined,Title_UploadVApp,Description,DesCannotBeEmpty,DesExceed,DidnotChoosed,Uploading,
	Compiling,Uploaded,Downloading,MoreSettings,VersionInfo,EditPermissions,VersionCode,VersionName,VersionCodeCannotBeEmpty,VersionNameCannotBeEmpty,EditSuccessfully,OpenSource,UpdateDes,Update,PkgLessThanTwo,
	LoginFirst;
	public static String get(int id) {
		return Language.get(id);
	}
	public static String[] gets(int...ids) {
		if (ids.length==0) return new String[0];
		String[] res=new String[ids.length];
		for (int i=0;i<ids.length;i++) res[i]=get(ids[i]);
		return res;
	}
	public static HashMap<Integer,String> Language=new HashMap<>();
	static {
		try {
			Field[] fs=L.class.getDeclaredFields();
			int i=0;
			for (Field one : fs) {
				if (one.getType() != int.class) continue;
				one.set(null, i++);
			}
		} catch (Exception e) {}
	}
}
