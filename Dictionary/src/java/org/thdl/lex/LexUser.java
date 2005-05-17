package org.thdl.lex;

import org.apache.commons.beanutils.BeanUtils;
import org.thdl.users.ThdlUser;

public class LexUser extends org.thdl.users.ThdlUser {
	
	public LexUser() {
	};
	
	public LexUser(ThdlUser user) throws Exception {
		BeanUtils.copyProperties(this, user);
	}
	
	public boolean isGuest() {
		return checkRole("guest");
	}

	public boolean isAdministrator() {
		return checkRole("admin");
	}

	public boolean isEditor() {
		return checkRole("editorHi") || checkRole("editorLo");
	}

	public boolean isDeveloper() {
		return checkRole("dev");
	}

	public boolean isProofer() {
		return checkRole("proofer");
	}
	
	public boolean isCanAddTerms() {
		return isAdministrator() || checkRole("editorHi") || isDeveloper();
	}
	
	public boolean isCanEdit() {
		return isAdministrator() || isDeveloper() || isProofer() || isEditor();
	}
	
	private boolean checkRole(String role) {
		boolean bool = false;
		if (hasRole(role)) {
			bool = true;
		}
		return bool;
	}

	public boolean isFingerprintless() {
		return isAdministrator() || isProofer();
	}
}