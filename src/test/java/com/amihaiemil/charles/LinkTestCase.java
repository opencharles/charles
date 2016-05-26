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

import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Test cases for {@link Link}
 * @author Mihai Andronache (amihaiemil@gmail.com)
 *
 */
public class LinkTestCase {
	/**
	 * A link can tell whether it's valid or not.
	 */
	@Test
    public void validatesSelfFalse() {
    	Link l = new Link("test", "www.amihaiemil.com/#");
    	assertFalse(l.valid("www.amihaiemil.com"));
    	Link l2 = new Link("test", "mailto:amihaiemil@gmail.com");
    	assertFalse(l2.valid("www.amihaiemil.com"));
    }
	/**
	 * A link can tell whether it's valid or not.
	 */
	@Test
    public void validatesSelfTrue() { 
		Link l = new Link("test", "www.amihaiemil.com/projects/2016/04/20/project-eva.html");
    	assertTrue(l.valid("www.amihaiemil.com"));
    	
    	Link l2 = new Link("test", "http://www.amihaiemil.com/projects/2016/04/20/project-eva.html");
    	assertTrue(l2.valid("http://www.amihaiemil.com"));
    	
    	Link l3 = new Link("test", "https://www.amihaiemil.com/projects/2016/04/20/project-eva.html");
    	assertTrue(l3.valid("https://www.amihaiemil.com"));
    }
}
