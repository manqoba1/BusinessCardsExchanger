package codetribe.sifiso.com.bcelibrary.toolbox;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import khandroid.ext.apache.http.util.ByteArrayBuffer;

public class BohaRequest extends Request<JSONObject> {

    private Listener<JSONObject> listener;
    private ErrorListener errorListener;
    private long start, end;

    public BohaRequest(int method, String url, ErrorListener listener) {
        super(method, url, listener);
    }

    public BohaRequest(int method, String url,
                       Listener<JSONObject> responseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = responseListener;
        this.errorListener = errorListener;
        start = System.currentTimeMillis();
        Log.i(LOG, "...Cloud Server communication started ...");

    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(
            NetworkResponse response) {
        JSONObject dto=null;
        try {
            Gson gson = new Gson();
            String resp = new String(response.data);
            Log.i(LOG, "response string length returned: " + resp.length());
            try {
                dto = new JSONObject(resp);
                Log.i(LOG, "response string length returned: " + dto.toString());
                if (dto != null) {
                    Log.i(LOG, "response string length returned2: " + dto.toString());
                    return Response.success(dto,
                            HttpHeaderParser.parseCacheHeaders(response));
                }
                Log.i(LOG, "response string length returned: " + dto.toString());
            } catch (Exception e) {
                // ignore, it's a zipped response
                Log.e(LOG, e.toString());
            }

            InputStream is = new ByteArrayInputStream(response.data);
            ZipInputStream zis = new ZipInputStream(is);
            @SuppressWarnings("unused")
            ZipEntry entry;
            ByteArrayBuffer bab = new ByteArrayBuffer(2048);

            while ((entry = zis.getNextEntry()) != null) {
                int size = 0;
                byte[] buffer = new byte[2048];
                while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
                    bab.append(buffer, 0, size);
                }
                resp = new String(bab.toByteArray());
                dto = new JSONObject(resp);
            }
        } catch (Exception e) {
            VolleyError ve = new VolleyError("Exception parsing server data", e);
            errorListener.onErrorResponse(ve);
            try {
                Log.e(LOG, "Unable to complete request: " + dto.getString("message"), e);
                return Response.error(new VolleyError(dto.getString("message")));
            } catch (Exception ex) {

            }
        }
        end = System.currentTimeMillis();
        Log.e(LOG, "#### comms elapsed time in seconds: " + getElapsed(start, end));
        return Response.success(dto,
                HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        end = System.currentTimeMillis();
        listener.onResponse(response);
    }

    public static double getElapsed(long start, long end) {
        BigDecimal m = new BigDecimal(end - start).divide(new BigDecimal(1000));
        return m.doubleValue();
    }

    static final String LOG = "BohaRequest";
}
