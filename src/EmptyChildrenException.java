/**
 * Created by Cwang on 9/14/16.
 */
public class EmptyChildrenException extends Throwable {
    public EmptyChildrenException(String s) {
        super("Empty children exception: "+s);
    }
}
