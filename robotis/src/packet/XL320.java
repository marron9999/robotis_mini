package packet;

public class XL320 {

	// https://emanual.robotis.com/docs/en/dxl/x/xl320/

	// Control Table of EEPROM Area
	public static final int[] _ModelNumber = {0, 2};		// Model Number	R	350	-	-
	public static final int[] _FirmwareVersion = {2, 1};	// Firmware Version	R	-	-	-
	public static final int[] _ID = {3, 1};					// DYNAMIXEL ID	RW	1	0	252
	public static final int[] _BaudRate = {4, 1};			// Communication Speed	RW	3	0	3
	public static final int[] _ReturnDelayTime = {5, 1};	// Response Delay Time	RW	250	0	254
	public static final int[] _CWAngleLimit = {6, 2};		// Clockwise Angle Limit	RW	0	0	1023
	public static final int[] _CCWAngleLimit = {8, 2};		// Counter-Clockwise Angle Limit	RW	1023	0	1023
	public static final int[] _ControlMode = {11, 1};		// Control Mode	RW	2	1	2
	public static final int[] _TemperatureLimit = {12, 1};	// Maximum Internal Temperature Limit	RW	65	0	150
	public static final int[] _MinVoltageLimit = {13, 1};	// Minimum Input Voltage Limit	RW	60	50	250
	public static final int[] _MaxVoltageLimit = {14, 1};	// Maximum Input Voltage Limit	RW	90	50	250
	public static final int[] _MaxTorque = {15, 2};			// Maximum Torque	RW	1023	0	1023
	public static final int[] _StatusReturnLevel = {17, 1};	// Select Types of Status Return	RW	2	0	2
	public static final int[] _Shutdown = {18, 1};			// Shutdown Error Information	RW	3	0	7

	//Control Table of RAM Area
	public static final int[] _TorqueEnable = {24, 1};		// Motor Torque On/Off	RW	0	0	1
	public static final int[] _LEDStatus = {25, 1};			// LED On/Off	RW	0	0	7
	public static final int[] _DGain = {27, 1};				// Derivative Gain	RW	0	0	254
	public static final int[] _IGain = {28, 1};				// Integral Gain	RW	0	0	254
	public static final int[] _PGain = {29, 1};				// Proportional Gain	RW	32	0	254
	public static final int[] _GoalPosition = {30, 2};		// Desired Position	RW	-	0	1023
	public static final int[] _MovingSpeed = {32, 2};		// Moving Speed(Moving Velocity)	RW	-	0	2047
	public static final int[] _TorqueLimit = {35, 2};		// Torque Limit	RW	-	0	1023
	public static final int[] _PresentPosition = {37, 2};	// Present Position	R	-	-	-
	public static final int[] _PresentSpeed = {39, 2};		// Present Speed	R	-	-	-
	public static final int[] _PresentLoad = {41, 2};		// Present Load	R	-	-	-
	public static final int[] _PresentVoltage = {45, 1};	// Present Voltage	R	-	-	-
	public static final int[] _PresentTemperature = {46, 1};// Present Temperature	R	-	-	-
	public static final int[] _RegisteredInstruction = {47, 1};	// If Instruction is registered	R	0	-	-
	public static final int[] _Moving = {49, 1};			// Movement Status	R	0	-	-
	public static final int[] _HardwareErrorStatus = {50, 1};// Hardware Error Status	R	0	-	-
	public static final int[] _Punch = {51, 2};				// Minimum Current Threshold	RW	32	0	1023

	// ID
	public static int MAX = 17;
	public final static int MIN = 1;

//	// LED
// 有効ビット	値	出力色
// (000)	0	オフ
// (001)	1	赤
// (010)	2	緑
// (100)	4	青
// (011)	3	黄色
// (110)	6	シアン
// (101)	5	紫
// (111)	7	白

// 注: LED は、デバイスの現在のステータスを示します。
// 起動中	赤色 LED が 1 回点滅
// 工場出荷時設定へのリセット	赤色 LED が 4 回点滅
// 警報	赤色 LED が点滅する
// 起動モード	赤色 LED 点灯

//	public static final int LED_OFF = 0;
//	public static final int LED_RED = 1;
//	public static final int LED_GREEN = 2;
//	public static final int LED_YELLOW = 1+2;
//	public static final int LED_BLUE = 4;
//	public static final int LED_PURPLE = 1+4;
//	public static final int LED_CYAN = 2+4;
//	public static final int LED_WHITE = 1+2+4;
}