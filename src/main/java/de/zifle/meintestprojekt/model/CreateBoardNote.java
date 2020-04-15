package de.zifle.meintestprojekt.model;


import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateBoardNote  {
  
  @ApiModelProperty(required = true, value = "")
  private String content;

  @ApiModelProperty(required = true, value = "")
  private Integer boardColumnId;
 /**
   * Get content
   * @return content
  **/
  @JsonProperty("content")
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public CreateBoardNote content(String content) {
    this.content = content;
    return this;
  }

 /**
   * Get boardColumnId
   * @return boardColumnId
  **/
  @JsonProperty("boardColumnId")
  public Integer getBoardColumnId() {
    return boardColumnId;
  }

  public void setBoardColumnId(Integer boardColumnId) {
    this.boardColumnId = boardColumnId;
  }

  public CreateBoardNote boardColumnId(Integer boardColumnId) {
    this.boardColumnId = boardColumnId;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateBoardNote {\n");
    
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    boardColumnId: ").append(toIndentedString(boardColumnId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private static String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

