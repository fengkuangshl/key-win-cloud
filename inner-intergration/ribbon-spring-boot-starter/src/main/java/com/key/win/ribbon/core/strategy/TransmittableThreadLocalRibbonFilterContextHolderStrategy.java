package com.key.win.ribbon.core.strategy;

/*
 * Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.alibaba.ttl.TransmittableThreadLocal;
import com.key.win.ribbon.core.context.DefaultRibbonFilterContext;
import com.key.win.ribbon.core.context.RibbonFilterContext;
import org.springframework.util.Assert;

/**
 * An <code>InheritableThreadLocal</code>-based implementation of
 * {@link org.springframework.security.core.context.RibbonFilterContexHolderStrategy}.
 *
 * @author Ben Alex
 *
 * @see java.lang.ThreadLocal
 */
public   class TransmittableThreadLocalRibbonFilterContextHolderStrategy implements
RibbonFilterContextHolderStrategy {
	// ~ Static fields/initializers
	// =====================================================================================

	private static final ThreadLocal<RibbonFilterContext> CONTEXT_HOLDER = new TransmittableThreadLocal<>();

	// ~ Methods
	// ========================================================================================================

	public void clearContext() {
		CONTEXT_HOLDER.remove();
	}

	public RibbonFilterContext getContext() {
		RibbonFilterContext ctx = CONTEXT_HOLDER.get();

		if (ctx == null) {
			ctx = createEmptyContext();
			CONTEXT_HOLDER.set(ctx);
		}

		return ctx;
	}

	public void setContext(RibbonFilterContext context) {
		Assert.notNull(context, "Only non-null RibbonFilterContex instances are permitted");
		CONTEXT_HOLDER.set(context);
	}

	public RibbonFilterContext createEmptyContext() {
		return new DefaultRibbonFilterContext();
	}
}
