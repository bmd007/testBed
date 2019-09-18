package ir.tiroon.mock.asset.domain;

import java.util.Objects;

public class IdSequence {

    private String id;

    private Long seq;

    public IdSequence(String id, Long seq) {
        this.id = id;
        this.seq = seq;
    }

    public Long getSeq() {
        return seq;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof IdSequence)) {
            return false;
        }
        IdSequence castOther = (IdSequence) other;
        return Objects.equals(id, castOther.id) && Objects.equals(seq, castOther.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, seq);
    }
}
