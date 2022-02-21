package stu.napls.nabootweb.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import stu.napls.nabootweb.core.response.Response;

/**
 * @Author Tei Michael
 * @Date 12/29/2019
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(value = SystemException.class)
    public Response systemExceptionHandler(SystemException e) {
        logger.error(e.getMessage());
        return Response.failure(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = InternalException.class)
    public Response internalExceptionHandler(InternalException e) {
        logger.error(e.getMessage());
        return Response.failure("Internal Error.");
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Response errorHandler(Exception e) {
        logger.error(e.getMessage());
        return Response.failure("Internal Error.");
    }


}
