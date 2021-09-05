package org.shaneking.leon.test;

import lombok.Getter;
import lombok.Setter;
import org.shaneking.ling.zero.io.File0;
import org.shaneking.ling.zero.lang.String0;
import org.shaneking.ling.zero.util.List0;
import org.shaneking.ling.zero.util.Map0;
import org.shaneking.ling.zero.util.UUID0;
import org.shaneking.roc.test.SKSpringUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.http.Cookie;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest
@AutoConfigureMockMvc
public class SKSpringMvcUnit extends SKSpringUnit {
  @Autowired
  @Getter
  public MockMvc mockMvc;
  @Getter
  @Setter
  private Map<String, Supplier<String>> tstReplaceMap = Map0.newHashMap(List0.newArrayList("$UUID0_cUl33_1$"), List0.newArrayList((Supplier<String>) UUID0::cUl33));
  @Getter
  @Setter
  private String tstUploadFileFieldName = "file";
  @Getter
  @Setter
  private String tstUploadParamFieldName = "req";
  @Getter
  @Setter
  private String tstUploadContentType = "text/plain";

  public MockHttpServletRequestBuilder cookie(MockHttpServletRequestBuilder mockHttpServletRequestBuilder) {
    return mockHttpServletRequestBuilder.cookie(new Cookie("skToken", "skTestToken"));
  }

  public MockHttpServletRequestBuilder appJson(MockHttpServletRequestBuilder mockHttpServletRequestBuilder) {
    return mockHttpServletRequestBuilder.contentType(MediaType.APPLICATION_JSON);
  }

  public MockHttpServletRequestBuilder multiFormData(MockHttpServletRequestBuilder mockHttpServletRequestBuilder) {
    return mockHttpServletRequestBuilder.contentType(MediaType.MULTIPART_FORM_DATA);
  }

  public ResultActions perform(String url, File reqJsonFile, File reqAttachFile, File respJsonFile, File respTxtFile, Charset charset) throws Exception {
    Map<String, String> replaceMap = Map0.newHashMap();
    this.getTstReplaceMap().keySet().forEach(k -> replaceMap.put(k, this.getTstReplaceMap().get(k).get()));
    String reqStr = String.join(String0.EMPTY, Files.readAllLines(reqJsonFile.toPath(), charset));
    for (String key : replaceMap.keySet()) {
      reqStr = reqStr.replace(key, replaceMap.get(key));
    }
    ResultActions resultActions = null;
    if (reqAttachFile == null) {
      resultActions = mockMvc.perform(appJson(cookie(post(url).content(reqStr))));
    } else {
      resultActions = mockMvc.perform(multiFormData(cookie(multipart(url).file(new MockMultipartFile(this.getTstUploadFileFieldName(), reqAttachFile.getName(), this.getTstUploadContentType(), Files.readAllBytes(reqAttachFile.toPath()))).param(this.getTstUploadParamFieldName(), reqStr))));
    }
    resultActions.andExpect(status().isOk()).andDo(print());
    if (respJsonFile != null) {
      String respStr = String.join(String0.EMPTY, Files.readAllLines(respJsonFile.toPath(), charset).stream().map(String::trim).collect(Collectors.toList()));
      respStr = respStr.replace("\": ", "\":");
      for (String key : replaceMap.keySet()) {
        respStr = respStr.replace(key, replaceMap.get(key));
      }
      resultActions.andExpect(content().string(respStr));
    } else if (respTxtFile != null) {
      for (String s : Files.readAllLines(respTxtFile.toPath(), charset)) {
        if (!String0.isNullOrEmpty(s)) {
          for (String key : replaceMap.keySet()) {
            s = s.replace(key, replaceMap.get(key));
          }
          resultActions.andExpect(content().string(containsString(s)));
        }
      }
    }
    return resultActions;
  }

  public ResultActions performJFJ(String url) throws Exception {
    return performJFJ(url, tstITxtFiles());
  }

  public ResultActions performJFT(String url) throws Exception {
    return performJFT(url, tstITxtFiles());
  }

  public ResultActions performJFJ(String url, File reqAttachFile) throws Exception {
    return performJFJ(url, reqAttachFile, StandardCharsets.UTF_8);
  }

  public ResultActions performJFJ(String url, File reqAttachFile, Charset charset) throws Exception {
    return perform(url, tstIJsonFiles(), reqAttachFile, tstOJsonFiles(), null, charset);
  }

  public ResultActions performJFT(String url, File reqAttachFile) throws Exception {
    return performJFT(url, reqAttachFile, StandardCharsets.UTF_8);
  }

  public ResultActions performJFT(String url, File reqAttachFile, Charset charset) throws Exception {
    return perform(url, tstIJsonFiles(), reqAttachFile, null, tstOTxtFiles(), charset);
  }

  public ResultActions performJJ(String url) throws Exception {
    return performJJ(url, StandardCharsets.UTF_8);
  }

  public ResultActions performJJ(String url, Charset charset) throws Exception {
    return perform(url, tstIJsonFiles(), null, tstOJsonFiles(), null, charset);
  }

  public ResultActions performJT(String url) throws Exception {
    return performJT(url, StandardCharsets.UTF_8);
  }

  public ResultActions performJT(String url, Charset charset) throws Exception {
    return perform(url, tstIJsonFiles(), null, null, tstOTxtFiles(), charset);
  }

  public File tstIJsonFiles() {
    return tstIFiles(File0.TYPE_JSON);
  }

  public File tstITxtFiles() {
    return tstIFiles(File0.TYPE_TXT);
  }

  public File tstOJsonFiles() {
    return tstOFiles(File0.TYPE_JSON);
  }

  public File tstOTxtFiles() {
    return tstOFiles(File0.TYPE_TXT);
  }
}
