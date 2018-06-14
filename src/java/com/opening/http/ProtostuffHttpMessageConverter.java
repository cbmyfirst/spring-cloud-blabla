/**
 * Copyright (C) Opening Information Technology, LTD. 
 * All Rights Reserved.
 *
 * ProtostuffHttpMessageConverter.java created on 12/6/2018 18:08:26 by Sandy Deng 
 */
package com.opening.http;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.opening.cache.serializer.ProtoWrapper;

/**
 * <pre>
 * should extends AbstractGenericHttpMessageConverter
 * @author Sandy Deng
 * @date 12/6/2018 18:08:26
 *
 * </pre>
 */
public class ProtostuffHttpMessageConverter extends AbstractGenericHttpMessageConverter<Object>
{
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    public static final MediaType MEDIA_TYPE = new MediaType("application", "x-protostuff", DEFAULT_CHARSET);
    public static final Schema<ProtoWrapper> schema = RuntimeSchema.createFrom(ProtoWrapper.class);
    
    public ProtostuffHttpMessageConverter()
    {
        super(MEDIA_TYPE);
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException,
            HttpMessageNotReadableException
    {
        return this.readInternal(contextClass, inputMessage);
    }

    @Override
    protected void writeInternal(Object src, Type type, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException
    {
        // head type is correct
        if (MEDIA_TYPE.isCompatibleWith(outputMessage.getHeaders().getContentType()))
        {
            try (final OutputStream stream = outputMessage.getBody())
            {
                ProtoWrapper wrapper = new ProtoWrapper();
                wrapper.data = src;
                ProtobufIOUtil.writeTo(stream, wrapper, schema,
                            LinkedBuffer.allocate()); 
            }
        }
        else
        {
            throw new HttpMessageNotWritableException("Unrecognized HTTP media type "
                    + outputMessage.getHeaders().getContentType().getType() + ".");
        }
        
    }

    @Override
    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException,
            HttpMessageNotReadableException
    {
        if (MEDIA_TYPE.isCompatibleWith(inputMessage.getHeaders().getContentType()))
        {

            ProtoWrapper value = schema.newMessage();

            try (final InputStream stream = inputMessage.getBody())
            {
                ProtobufIOUtil.mergeFrom(stream, value, schema);

                return value.data;
            }
        }

        throw new HttpMessageNotReadableException("Unrecognized HTTP media type "
                + inputMessage.getHeaders().getContentType().getType() + ".");
    }

}
