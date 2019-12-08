package sk.svb.yi_dash_camera.config;

public class YiDashCameraUrls {

    /**
     * successful response:
     *
     * <?xml version="1.0" encoding="UTF-8" ?>
     * <Function>
     * <Cmd>8001</Cmd>
     * <Status>0</Status>
     * <Vendor>C10</Vendor>
     * </Function>
     */
    public static final String URL_STREAM_OPEN = "http://192.168.1.254/?custom=1&cmd=8001";

    /**
     * successful response:
     *
     * <?xml version="1.0" encoding="UTF-8" ?>
     * <Function>
     * <Cmd>3001</Cmd>
     * <Status>0</Status>
     * </Function>
     */
    public static final String URL_FILE_COMMAND = "http://192.168.1.254/?custom=1&cmd=3001&par=2";

    /**
     * successful response:
     *
     * <LIST>
     * <ALLFile><File>
     * <NAME>2019_1101_230441.MP4</NAME>
     * <FPATH>A:\YICarCam\Movie\2019_1101_230441.MP4</FPATH>
     * <SIZE>852591</SIZE>
     * <TIME>2019/11/01 23:04:40</TIME>
     * <ATTR>32</ATTR><SUB>TRUE</SUB><SUBSIZE>440735</SUBSIZE></File>
     * </ALLFile>
     * <ALLFile><File>
     * <NAME>2019_1101_230328.MP4</NAME>
     * <FPATH>A:\YICarCam\Movie\2019_1101_230328.MP4</FPATH>
     * <SIZE>48922012</SIZE>
     * <TIME>2019/11/01 23:03:28</TIME>
     * <ATTR>32</ATTR><SUB>TRUE</SUB><SUBSIZE>18893292</SUBSIZE></File>
     * </ALLFile>
     * ...
     * </LIST>
     */
    public static final String URL_GET_FILES = "http://192.168.1.254/?custom=1&cmd=3015";
}
