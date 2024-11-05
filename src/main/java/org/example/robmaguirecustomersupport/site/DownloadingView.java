package org.example.robmaguirecustomersupport.site;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.View;

import java.util.Map;

public record DownloadingView(String filename, byte[] contents) implements View {
    @Override
    public String getContentType() {
        return "application/octet-stream";
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setContentType(getContentType());
        response.getOutputStream().write(contents);
    }
}
