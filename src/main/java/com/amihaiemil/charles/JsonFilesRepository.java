/**
 * Copyright (c) 2016-2017, Mihai Emil Andronache
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * * Neither the name of charles nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.amihaiemil.charles;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Exports each page into the specified file, in json format.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 *
 */
public final class JsonFilesRepository implements Repository {
    private static final Logger LOG = LoggerFactory.getLogger(JsonFilesRepository.class);

    /**
     * Directory where the json files should be stored.
     */
    private String dir;
    
    /**
     * Constructor.
     * @param dir Directory where the json files should be stored.
     */
    public JsonFilesRepository(String dir) {
        this.dir = dir;
        if(!dir.endsWith("/")) {
            this.dir += "/";
        }
    }

    /**
     * Export.
     * @param pages Pages to export.
     * @throws DataExportException If something goes wrong.
     */
    public void export(List<WebPage> pages) throws DataExportException {
        for(WebPage page : pages){
            ObjectMapper jsonMapper = new ObjectMapper();
            jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
            try {
                File jsonFile = new File(this.dir + page.getName() + ".json");
                if(!jsonFile.exists()) {
                    jsonFile.createNewFile();
                } else {
                    jsonFile.delete();
                }
                jsonMapper.writeValue(jsonFile, page);
            } catch (JsonGenerationException e) {
                LOG.error(e.getMessage(), e);
                throw new DataExportException(
                    "Page with url " + page.getUrl() + " could not be exported! Check the logs for errors.");
            } catch (JsonMappingException e) {
                LOG.error(e.getMessage(), e);
                throw new DataExportException(
                    "Page with url " + page.getUrl() + " could not be exported! Check the logs for errors.");
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
                throw new DataExportException(
                    "Page with url " + page.getUrl() + " could not be exported! Check the logs for errors.");
            }
        }
    }

}
