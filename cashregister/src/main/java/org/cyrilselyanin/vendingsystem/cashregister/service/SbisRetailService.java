package org.cyrilselyanin.vendingsystem.cashregister.service;

import org.cyrilselyanin.vendingsystem.cashregister.dto.SbisRegCashRequestDto;
import org.cyrilselyanin.vendingsystem.cashregister.dto.SbisRegCashResponseDto;

import java.io.IOException;

/**
 * Retail service of the Sbis-adapter
 * author Cyril Selyanin
 */
public interface SbisRetailService {
    /**
     * Reistering cash
     * @param regCashUrl Sbis retail string url
     * @param token Sbis service token
     * @param sid Sbis session id
     * @param sbisRegCashRequestDto Request dto
     * @return Response dto
     * @throws NullPointerException
     * @throws IOException
     */
    SbisRegCashResponseDto regCash(
        String regCashUrl,
        String token,
        String sid,
        SbisRegCashRequestDto sbisRegCashRequestDto
    ) throws NullPointerException, IOException;
}
