package packet;

public class SyncWriteParam {
    public byte[] data;
    public int id;

    public SyncWriteParam(int id, byte... data) {
        this.id = id;
        this.data = data;
    }
}
