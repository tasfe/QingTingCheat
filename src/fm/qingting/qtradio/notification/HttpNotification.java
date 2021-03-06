package fm.qingting.qtradio.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

public class HttpNotification
{
  private static HttpNotification _instance;
  private SharedPreferences sharedPrefs;

  private String connect(HttpUriRequest paramHttpUriRequest)
    throws IOException
  {
    Object localObject1 = null;
    paramHttpUriRequest.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:5.0) Gecko/20100101 Firefox/5.0");
    paramHttpUriRequest.addHeader("Accept-Encoding", "gzip");
    String str1 = getLastModified();
    if (str1 != null)
      paramHttpUriRequest.addHeader("if-modified-since", str1);
    paramHttpUriRequest.getURI().getHost();
    BasicHttpParams localBasicHttpParams = new BasicHttpParams();
    HttpConnectionParams.setConnectionTimeout(localBasicHttpParams, 60000);
    HttpConnectionParams.setSoTimeout(localBasicHttpParams, 60000);
    DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient(localBasicHttpParams);
    HttpResponse localHttpResponse1;
    try
    {
      HttpResponse localHttpResponse2 = localDefaultHttpClient.execute(paramHttpUriRequest);
      localHttpResponse1 = localHttpResponse2;
      if (localHttpResponse1 == null)
        return "";
    }
    catch (ClientProtocolException localClientProtocolException)
    {
      localClientProtocolException.printStackTrace();
      return "";
    }
    catch (SocketTimeoutException localSocketTimeoutException)
    {
      localSocketTimeoutException.printStackTrace();
      return "";
    }
    catch (IOException localIOException1)
    {
      while (true)
      {
        localIOException1.printStackTrace();
        if (localIOException1.getClass().toString().indexOf("ConnectTimeoutException") != -1)
          return "";
        if (localIOException1.getClass().toString().indexOf("UnknownHostException") != -1)
          return "";
        localHttpResponse1 = null;
      }
    }
    catch (IllegalStateException localIllegalStateException)
    {
      localIllegalStateException.printStackTrace();
      return "";
    }
    HttpEntity localHttpEntity = localHttpResponse1.getEntity();
    if ((localHttpResponse1.getStatusLine() != null) && (localHttpResponse1.getStatusLine().getStatusCode() == 304))
      return "304";
    if (localHttpEntity != null)
    {
      String str2 = "";
      try
      {
        localObject1 = localHttpEntity.getContent();
        Header localHeader = localHttpResponse1.getFirstHeader("Content-Encoding");
        if ((localHeader != null) && (localHeader.getValue().equalsIgnoreCase("gzip")))
          localObject1 = new GZIPInputStream((InputStream)localObject1);
        if (localHttpResponse1.containsHeader("Last-Modified"))
          str2 = localHttpResponse1.getFirstHeader("Last-Modified").getValue();
        saveLastModified(str2);
        localByteArrayOutputStream = new ByteArrayOutputStream();
        byte[] arrayOfByte = new byte[1024];
        while (true)
        {
          int i = ((InputStream)localObject1).read(arrayOfByte, 0, arrayOfByte.length);
          if (i == -1)
            break;
          localByteArrayOutputStream.write(arrayOfByte, 0, i);
        }
      }
      catch (ParseException localParseException)
      {
        ByteArrayOutputStream localByteArrayOutputStream;
        localParseException.printStackTrace();
        return "";
        String str3 = new String(localByteArrayOutputStream.toByteArray());
        return str3;
      }
      catch (IOException localIOException2)
      {
        localIOException2.printStackTrace();
        return "";
      }
      finally
      {
        if (localObject1 != null)
          ((InputStream)localObject1).close();
        localHttpEntity.consumeContent();
      }
    }
    return "";
  }

  public static HttpNotification getInstance()
  {
    if (_instance == null)
      _instance = new HttpNotification();
    return _instance;
  }

  private String getLastModified()
  {
    if (this.sharedPrefs == null)
      return "000000";
    return this.sharedPrefs.getString("lastmodified", "000000");
  }

  private void saveLastModified(String paramString)
  {
    if ((paramString == null) || (this.sharedPrefs == null))
      return;
    SharedPreferences.Editor localEditor = this.sharedPrefs.edit();
    localEditor.putString("lastmodified", paramString);
    localEditor.commit();
  }

  public String getNotify(String paramString, Map<String, Object> paramMap, Map<String, String> paramMap1)
  {
    HttpGet localHttpGet;
    try
    {
      localHttpGet = new HttpGet(paramString);
      if (paramMap1 != null)
      {
        Iterator localIterator = paramMap1.keySet().iterator();
        while (localIterator.hasNext())
        {
          String str2 = (String)localIterator.next();
          localHttpGet.setHeader(str2, (String)paramMap1.get(str2));
        }
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      return "";
    }
    String str1 = connect(localHttpGet);
    return str1;
  }

  public void init(Context paramContext)
  {
    if (paramContext == null)
      return;
    this.sharedPrefs = paramContext.getSharedPreferences("client_preferences", 0);
  }
}

/* Location:           /Users/zhangxun-xy/Downloads/qingting2/classes_dex2jar.jar
 * Qualified Name:     fm.qingting.qtradio.notification.HttpNotification
 * JD-Core Version:    0.6.2
 */