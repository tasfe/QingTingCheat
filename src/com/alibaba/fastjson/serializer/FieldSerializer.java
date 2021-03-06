package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.FieldInfo;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class FieldSerializer
{
  private final String double_quoted_fieldPrefix;
  protected final FieldInfo fieldInfo;
  private final String single_quoted_fieldPrefix;
  private final String un_quoted_fieldPrefix;
  private boolean writeNull = false;

  public FieldSerializer(FieldInfo paramFieldInfo)
  {
    this.fieldInfo = paramFieldInfo;
    paramFieldInfo.setAccessible(true);
    this.double_quoted_fieldPrefix = ('"' + paramFieldInfo.getName() + "\":");
    this.single_quoted_fieldPrefix = ('\'' + paramFieldInfo.getName() + "':");
    this.un_quoted_fieldPrefix = (paramFieldInfo.getName() + ":");
    JSONField localJSONField = (JSONField)paramFieldInfo.getAnnotation(JSONField.class);
    if (localJSONField != null)
    {
      SerializerFeature[] arrayOfSerializerFeature = localJSONField.serialzeFeatures();
      int i = arrayOfSerializerFeature.length;
      for (int j = 0; j < i; j++)
        if (arrayOfSerializerFeature[j] == SerializerFeature.WriteMapNullValue)
          this.writeNull = true;
    }
  }

  public Field getField()
  {
    return this.fieldInfo.getField();
  }

  public Method getMethod()
  {
    return this.fieldInfo.getMethod();
  }

  public String getName()
  {
    return this.fieldInfo.getName();
  }

  public Object getPropertyValue(Object paramObject)
    throws Exception
  {
    return this.fieldInfo.get(paramObject);
  }

  public boolean isWriteNull()
  {
    return this.writeNull;
  }

  public void writePrefix(JSONSerializer paramJSONSerializer)
    throws IOException
  {
    SerializeWriter localSerializeWriter = paramJSONSerializer.getWriter();
    if (paramJSONSerializer.isEnabled(SerializerFeature.QuoteFieldNames))
    {
      if (paramJSONSerializer.isEnabled(SerializerFeature.UseSingleQuotes))
      {
        localSerializeWriter.write(this.single_quoted_fieldPrefix);
        return;
      }
      localSerializeWriter.write(this.double_quoted_fieldPrefix);
      return;
    }
    localSerializeWriter.write(this.un_quoted_fieldPrefix);
  }

  public abstract void writeProperty(JSONSerializer paramJSONSerializer, Object paramObject)
    throws Exception;

  public abstract void writeValue(JSONSerializer paramJSONSerializer, Object paramObject)
    throws Exception;
}

/* Location:           /Users/zhangxun-xy/Downloads/qingting2/classes_dex2jar.jar
 * Qualified Name:     com.alibaba.fastjson.serializer.FieldSerializer
 * JD-Core Version:    0.6.2
 */