# **模型上下文协议MCP**
* **[MCP官方文档](https://modelcontextprotocol.io/docs/getting-started/intro)**

### **一、什么是模型上下文协议（MCP）？**
- MCP （Model Context Protocol）是一个开源标准协议，用于将AI应用程序连接到外部系统。
- 使用MCP，像Claude或ChatGPT这样的人工智能应用程序可以连接到数据源（例如本地文件，数据库），工具（例如搜索引擎，计算器）和工作流（例如专门的提示）-使它们能够访问关键信息并执行任务。
- 可以把MCP想象成AI应用程序的USB-C端口。就像USB-C提供了一种连接电子设备的标准化方式一样，MCP提供了一种将AI应用程序连接到外部系统的标准化方式。
![MCP](src/AI框架/MCP/PictureResources/MCP原理图.png)

### **二、MCP体系架构**
MCP主机通过为每个MCP服务器创建一个MCP客户端来实现这一点。每个MCP客户端与相应的MCP服务器保持一对一的专用连接。
- MCP由两部分组成：**MCP客户端**和**MCP服务器**。
- MCP客户端是AI应用程序，它使用MCP协议与MCP服务器通信。
- MCP服务器是外部系统，它提供数据源，工具和工作流，并使用MCP协议与MCP客户端通信。

MCP架构的主要参与者有:
* MCP主机：协调和管理一个或多个MCP客户端的AI应用程序
* MCP客户端：维护与MCP服务器的连接，并从MCP服务器获取上下文以供MCP主机使用的组件
* MCP服务器：为MCP客户端提供上下文的程序

<img src="src/AI框架/MCP/PictureResources/架构图1.png" width="50%"><img src="src/AI框架/MCP/PictureResources/架构图2.png" width="50%">

##### **一般执行流程**
<img src="src/AI框架/MCP/PictureResources/流程图.png" width="200" height="350">

### **三、MCP Server**


### **四、MCP Client**
