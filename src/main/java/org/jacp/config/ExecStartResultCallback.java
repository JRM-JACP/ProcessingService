package org.jacp.config;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author saffchen created on 04.03.2024
 */

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class ExecStartResultCallback extends ResultCallback.Adapter<Frame> {

    private OutputStream stdout;
    private OutputStream stderr;

    @Override
    public void onNext(Frame frame) {
        if (frame != null) {
            try {
                switch (frame.getStreamType()) {
                    case STDOUT, RAW -> {
                        if (stdout != null) {
                            stdout.write(frame.getPayload());
                            stdout.flush();
                        }
                    }
                    case STDERR -> {
                        if (stderr != null) {
                            stderr.write(frame.getPayload());
                            stderr.flush();
                        }
                    }
                    default -> log.error("unknown stream type:" + frame.getStreamType());
                }
            } catch (IOException e) {
                onError(e);
            }
            log.debug(frame.toString());
        }
    }
}