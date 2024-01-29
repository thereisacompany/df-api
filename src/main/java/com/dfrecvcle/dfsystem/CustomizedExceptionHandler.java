package com.dfrecvcle.dfsystem;

import com.alibaba.fastjson.JSONObject;
import com.dfrecvcle.dfsystem.exception.BusinessRunTimeException;
import com.dfrecvcle.dfsystem.exception.InternalServerError;
import com.dfrecvcle.dfsystem.utils.HttpUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

//	@Autowired
//	private Log mLog;
	
	
//	public Log getLog() {  //final 的修飾詞可能導致autowired後面的動作無法更新funciton的mLog參考,導致一直為null
//		return mLog;
//	}


	/*
	 * 
	 * 若是此區塊又噴錯,會給tomcat抓取錯誤,回應訊息就是tomcat定義
	 * 
	 * 如以下訊息：（可以加上 throw new RuntimeException 就可以測試該情況） "timestamp":
	 * "2020-10-30T08:06:08.813+00:00", "status": 500, "error":
	 * "Internal Server Error", "trace":
	 * "java.lang.RuntimeException\n\tat com.chungyo.server.Controller.responseEcho(Controller.java:37)\n\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.lang.reflect.Method.invoke(Method.java:498)\n\tat org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:190)\n\tat org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:138)\n\tat org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:105)\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:878)\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:792)\n\tat org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\n\tat org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1040)\n\tat org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:943)\n\tat org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)\n\tat org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898)\n\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:626)\n\tat org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)\n\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:733)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n\tat org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n\tat org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n\tat org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n\tat org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202)\n\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97)\n\tat org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:541)\n\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:143)\n\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)\n\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78)\n\tat org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343)\n\tat org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:374)\n\tat org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)\n\tat org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:868)\n\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1590)\n\tat org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\n\tat java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)\n\tat java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)\n\tat org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\n\tat java.lang.Thread.run(Thread.java:748)\n"
	 * , "message": "No message available", "path": "/v1/echo" }
	 */

	//============================處理 特定型別的例外處理============================================================================
	// -------------自己丟出來的例外-----------
	@ExceptionHandler(BusinessRunTimeException.class)
	public final ResponseEntity<Object> handleBaseException(BusinessRunTimeException baseException, WebRequest request) {

		doExceptionHandle(baseException);
		ResponseEntity<Object> responseEntity = baseException.generateResponseEntity();
		return responseEntity;
	}

	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllException(Exception e, WebRequest request) {
		BusinessRunTimeException baseException = new InternalServerError(e.getMessage());
		baseException.initCause(e);
		doExceptionHandle(baseException);
		ResponseEntity<Object> responseEntity = baseException.generateResponseEntity();
		return responseEntity;
	}

    //============================複寫ResponseEntityExceptionHandler例外處理============================================================================
	// -------------不存在command的例外----------
	@Override
//	@QueueAnnotation
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		BusinessRunTimeException baseException = new BusinessRunTimeException(status, "Not Found command");
		baseException.initCause(e);

		doExceptionHandle(baseException);
		ResponseEntity<Object> responseEntity = baseException.generateResponseEntity();

		return responseEntity;

	}

	/*
	 * 
	 * 1. 缺少 required header => handleServletRequestBindingException 2.
	 * /v1/say沒給body資料 =>handleHttpMessageNotReadable *
	 * Http請求響應報文其實都是字串，當請求報文到java程式會被封裝為一個ServletInputStream流，開發人員再讀取報文，
	 * 響應報文則通過ServletOutputStream流，來輸出響應報文。 從流中只能讀取到原始的字串報文，同樣輸出流也是。那麼在報文到達SpringMVC
	 * / SpringBoot和從SpringMVC / SpringBoot出去，都存在一個字串到java物件的轉化問題。
	 * 
	 * 
	 * 2.驗證物件
	 * handleMethodArgumentNotValid
	 * 
	 * ResponseEntityExceptionHandler沒複寫的例外處理都會由該function處理
	 */

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String message = ex.getMessage();
		BusinessRunTimeException baseException = new BusinessRunTimeException(status, message);
		baseException.initCause(ex);
		doExceptionHandle(baseException);
		ResponseEntity<Object> responseEntity = baseException.generateResponseEntity();
		return responseEntity;
	}

    //============================ private function ============================================================================
	private void doExceptionHandle(BusinessRunTimeException baseException) {
		recordErrorRequestLog();
//		getLog().recordStackTrace(baseException);
		recordServerErrorResponseLog(baseException);
	}

	private void recordErrorRequestLog() {
		JSONObject jsonReqest = HttpUtil.getRequest();
//		getLog().errorLog(jsonReqest);
	}

	private void recordServerErrorResponseLog(BusinessRunTimeException baseException) {
		String resposnse = baseException.toString();
//		getLog().errorLog(resposnse);
//		getLog().serverLog(resposnse);
	}
}
