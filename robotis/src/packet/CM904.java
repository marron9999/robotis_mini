package packet;

public class CM904 {

	// https://emanual.robotis.com/docs/en/parts/controller/opencm904_jp/

	// ID
	public static final byte ID = (byte) 200;

//	// EEPROM
//	public static final byte[] _ModelNumber = {0, 2};	// モデル番号	R	400
//	public static final byte[] _FirmwareVersion = {6, 1};	// ファームウェア バージョン	R	-
//	public static final byte[] _ID = {7, 1};				// コントローラID	RW	200
//	public static final byte[] _BaudRate = {8, 1};			// 通信ボーレート	R	1
//	public static final byte[] _ReturnDelayTime= {9, 1};	// 応答待ち時間	RW	0
//	public static final byte[] _StatusReturnLevel = {10, 1};	// リターンパケットのレベル選択	RW	2
//	public static final byte[] _BootloaderVersion = {11, 1};	// ブートローダ バージョン	R	-
//	public static final byte[] _DXLBaudRate = {12, 1};		// DYNAMIXELとの通信ボーレート	RW	3
//	public static final byte[] _DYNAMIXELChannel = {16, 1};	// DYNAMIXELとの通信ポート選択	RW	0

	// RAM
	public static final byte[] _ModeNumber = {21, 1};		// 動作モード	RW	-
	public static final byte[] _ButtonStatus = {26, 1};		// スタートボタンの状態	R	0
	public static final byte[] _MotionPlayPage = {66, 2};	// Motion Play ページ番号	RW	0
	public static final byte[] _MotionPlayStatus = {68, 1};	// Motion Play ステータス	R	-
	public static final byte[] _TimerValue_128ms = {73, 1};	// 128ms タイマカウンタ	RW	0
	public static final byte[] _TimerValue_1ms = {74, 2};	// 1ms  タイマカウンタ	RW	0
	public static final byte[] _RandomNumber = {77, 1};		// 乱数値の生成	RW	-
	public static final byte[] _GreenLED = {79, 1};			// 緑LEDのステータス	RW	0
	public static final byte[] _MotionLED = {82, 1};		// Motion LEDのステータス	RW	0
	// 360	2	Port 1 IR Sensor Value	ポート1の赤外線センサ値	R	-
	// 366	2	Port 4 IR Sensor Value	ポート4の赤外線センサ値	R	-
	// 368	2	Port 1 DMS Sensor Value	ポート1の距離センサ値	R	-
	// 370	2	Port 2 DMS Sensor Value	ポート2の距離センサ値	R	-
	// 372	2	Port 3 DMS Sensor Value	ポート3の距離センサ値	R	-
	// 374	2	Port 4 DMS Sensor Value	ポート4の距離センサ値	R	-
	// 376	1	Port 1 Touch Sensor Value	ポート1のタッチセンサ値	R	-
	// 377	1	Port 2 Touch Sensor Value	ポート2のタッチセンサ値	R	-
	// 378	1	Port 3 Touch Sensor Value	ポート3のタッチセンサ値	R	-
	// 379	1	Port 4 Touch Sensor Value	ポート4のタッチセンサ値	R	-
	// 381	1	Port 2 LED Module Value	ポート2のLEDモジュール値	RW	0
	// 382	1	Port 3 LED Module Value	ポート3のLEDモジュール値	RW	0
	// 386	2	Port 2 User Device Value	ポート2のユーザーデバイス値	RW	0
	// 388	2	Port 3 User Device Value	ポート3のユーザーデバイス値	RW	0
	// 392	1	Port 1 Temperature Sensor Value	ポート1の温度センサ値	R	-
	// 393	1	Port 2 Temperature Sensor Value	ポート2の温度センサ値	R	-
	// 394	1	Port 3 Temperature Sensor Value	ポート3の温度センサ値	R	-
	// 395	1	Port 4 Temperature Sensor Value	ポート4の温度センサ値	R	-
	// 396	1	Port 1 Ultrasonic Sensor Value	ポート1の超音波センサ値	R	-
	// 397	1	Port 2 Ultrasonic Sensor Value	ポート2の超音波センサ値	R	-
	// 398	1	Port 3 Ultrasonic Sensor Value	ポート3の超音波センサ値	R	-
	// 399	1	Port 4 Ultrasonic Sensor Value	ポート4の超音波センサ値	R	-
	// 400	1	Port 1 Magnetic Sensor Value	ポート1の磁気センサ値	R	-
	// 401	1	Port 2 Magnetic Sensor Value	ポート2の磁気センサ値	R	-
	// 402	1	Port 3 Magnetic Sensor Value	ポート3の磁気センサ値	R	-
	// 403	1	Port 4 Magnetic Sensor Value	ポート4の磁気センサ値	R	-
	// 404	1	Port 1 Motion Sensor Value	ポート1のモーションセンサ値	R	-
	// 405	1	Port 2 Motion Sensor Value	ポート2のモーションセンサ値	R	-
	// 406	1	Port 3 Motion Sensor Value	ポート3のモーションセンサ値	R	-
	// 407	1	Port 4 Motion Sensor Value	ポート4のモーションセンサ値	R	-
	// 409	1	Port 2 Color Sensor Value	ポート2のカラーセンサ値	R	-
	// 410	1	Port 3 Color Sensor Value	ポート3のカラーセンサ値	R	-

//	public static int[] sensor_dms = new int[4];
//	public static int[] sensor_user = new int[4];

//	// PORT-NO
//	public static int IR;
//	public static int GYRO_X;
//	public static int GYRO_Y;
}
