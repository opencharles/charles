/*
 Copyright (c) 2016, Mihai Emil Andronache
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.
 * Neither the name of charles nor the names of its
 contributors may be used to endorse or promote products derived from
 this software without specific prior written permission.
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.amihaiemil.charles;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Contains url patterns that should be ignored (not crawled).
 * @author Mihai Andronache (amihaiemil@gmail.com)
 *
 */
public final class IgnoredPatterns {
    private List<String> patterns;
    
    public IgnoredPatterns() {
    	this(new ArrayList<String>());
    }
    
    public IgnoredPatterns(List<String> patterns) {
    	this.patterns = new ArrayList<String>();
    	for(String pattern : patterns) {
    		this.patterns.add(pattern);
    	}
    }
    
    /**
     * Should the given url be ignored or not?
     * @param url Tested url.
     * @return ture if it matches any pattern, false otherwise.
     */
    public boolean contains(String url) {
    	for(String p : patterns) {
    		if(p.equalsIgnoreCase(url)) {
    			return true;
    		}    		
    		if(matchesAsteriskPattern(p.trim(), url)){
    			return true;
    		}
    	    if(matchesRegex(p.trim(), url)) {
    	    	return true;
    	    }
    	}
    	return false;
    }
    
    /**
     * Test against asterisk pattern (e.g. *.js) 
	 * @param p pattern.
     * @param url Tested url string.
     * @return True if it matches.
     */
    private boolean matchesAsteriskPattern(String p, String url) {
    	if(p.contains("*")) {
    		List<Integer> partsIndexes = new ArrayList<Integer>();
    		String[] parts = p.split("\\*");
    		for(int i=0;i<parts.length;i++) {
    			if(url.contains(parts[i])) {
    				int indexOfPart = url.indexOf(parts[i]);
    				for(Integer partIndex : partsIndexes) {
    					if(partIndex > indexOfPart) {
    						return false;
    					}
    				}
    				partsIndexes.add(indexOfPart);
    			} else {
    				return false;
    			}
    		}
    	} else {
    		return false;
    	}
    	
    	return true;
    }
    
    /**
     * Test against regex.
     * @param p pattern.
     * @param url Tested url string.
     * @return True if it matches.
     */
    private boolean matchesRegex(String p, String url) {
    	try {
    		Pattern pattern = Pattern.compile(p);
    		Matcher matcher = pattern.matcher(url);
    		return matcher.matches();
    	} catch (PatternSyntaxException ex) {
    		return false;
    	}
    }
}
