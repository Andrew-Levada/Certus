package com.andrewlevada.certus.logic.livepapers;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewlevada.certus.R;
import com.andrewlevada.certus.tools.SimpleInflater;

import java.util.List;
import java.util.Map;

public class LivePaper {
    private Context context;
    private View.OnClickListener onClickListener;

    public LivePaper(Context context) {
        this.context = context;
        onClickListener = null;
    }

    public LivePaper(Context context, View.OnClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
    }

    public void process(ViewGroup parent, Map<String, Object> node, int index) {
        String type = (String) node.get("t");
        if (type == null) return;

        // If view has to be created
        if (parent.getChildCount() >= index)
            parent.addView(createView(parent, node));

        // If view doesn't need any update
        if (getHashCode(parent.getChildAt(index)) == getHashCode(node)) return;

        if (parent.getChildAt(index).getTag().equals(type)) {
            // If view updates content
            updateView(parent.getChildAt(index), node);
        } else {
            // If view needs to be replaced
            parent.removeViewAt(index);
            parent.addView(createView(parent, node), index);
        }
    }

    private View createView(ViewGroup parent, Map<String, Object> node) {
        View view = null;
        String type = (String) node.get("t");

        if (type.equals("text")) {
            view = addText(parent, node, false);
        } else if (type.equals("block")) {
            view = addBlock(parent, node);
        } else if (type.equals("header")) {
            view = addText(parent, node, true);
        } else if (type.equals("list")) {
            view = addList(parent, node);
        } else if (type.equals("rule")) {
            view = addRule(parent, node);
        }

        if (onClickListener != null && view != null) view.setOnClickListener(onClickListener);

        return view;
    }

    private void updateView(View view, Map<String, Object> node) {
        String type = (String) node.get("t");
        if (type == null) return;

        if (type.equals("text")) {
            fillText((TextView) view, node);
        } else if (type.equals("block")) {
            fillBlock((ViewGroup) view, node);
        } else if (type.equals("header")) {
            fillText((TextView) view, node);
        } else if (type.equals("list")) {
            fillList((ViewGroup) view, node);
        } else if (type.equals("rule")) {
            fillRule((ViewGroup) view, node);
        }
    }

    private View addText(ViewGroup parent, Map<String, Object> node, boolean isHeader) {
        TextView view;
        if (isHeader) view = (TextView) SimpleInflater.inflate(parent, R.layout.lp_header, false);
        else view = (TextView) SimpleInflater.inflate(parent, R.layout.lp_text, false);

        fillText(view, node);

        return view;
    }

    private View addBlock(ViewGroup parent, Map<String, Object> node) {
        ViewGroup view = (ViewGroup) SimpleInflater.inflate(parent, R.layout.lp_block, false);
        fillBlock(view, node);

        return view;
    }

    private View addList(ViewGroup parent, Map<String, Object> node) {
        ViewGroup view = (ViewGroup) SimpleInflater.inflate(parent, R.layout.lp_block, false);
        view.setTag("list");
        fillList(view, node);

        return view;
    }

    private View addRule(ViewGroup parent, Map<String, Object> node) {
        ViewGroup view = (ViewGroup) SimpleInflater.inflate(parent, R.layout.lp_rule_box, false);
        fillRule(view, node);

        return view;
    }

    private void fillText(TextView view, Map<String, Object> node) {
        Object text = node.get("v");
        SpannableStringBuilder stringBuilder = getStringBuilder(text);
        view.setText(stringBuilder);
    }

    private void fillBlock(ViewGroup view, Map<String, Object> node) {
        int i = 0;
        for (Map<String, Object> o : (List<Map<String, Object>>) node.get("v")) {
            process(view, o, i);
            i++;
        }

        for (int j = view.getChildCount() - 1; j >= ((List) node.get("v")).size(); j--) {
            view.removeViewAt(j);
        }
    }

    private void fillList(ViewGroup view, Map<String, Object> node) {
        String style = (String) node.get("s");
        if (style == null) style = "bullet";

        if (view.getChildCount() > 0 && !view.getChildAt(0).getTag().equals(style)) {
            for (int i = view.getChildCount() - 1; i >= 0; i--)
                view.removeViewAt(i);
        }

        int i = 0;
        for (Object o : (List) node.get("v")) {
            View v = null;
            if (i < view.getChildCount()) v = view.getChildAt(i);
            else if (style.equals("bullet"))
                v = SimpleInflater.inflate(view, R.layout.lp_list_bullet);
            else if (style.equals("num")) v = SimpleInflater.inflate(view, R.layout.lp_list_num);
            if (v == null) return;

            SpannableStringBuilder stringBuilder = getStringBuilder(o);
            ((TextView) v.findViewById(R.id.lp_list_text)).setText(stringBuilder);

            if (style.equals("num"))
                ((TextView) v.findViewById(R.id.lp_list_num)).setText((i + 1) + ".");

            i++;
        }

        for (int j = view.getChildCount() - 1; j > i; j--)
            view.removeViewAt(j);
    }

    private void fillRule(ViewGroup view, Map<String, Object> node) {
        int i = 0;
        for (Map<String, Object> o : (List<Map<String, Object>>) node.get("v")) {
            process(view, o, i);
            i++;
        }

        for (int j = view.getChildCount() - 1; j >= ((List) node.get("v")).size(); j--) {
            view.removeViewAt(j);
        }
    }

    private SpannableStringBuilder getStringBuilder(Object text) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();

        if (text instanceof String) stringBuilder.append((String) text);
        else if (text instanceof List) {
            for (Object o : (List<Object>) text) {
                if (o instanceof String) {
                    stringBuilder.append(o + " ");
                    continue;
                }

                Map m = (Map) o;
                String style = (String) m.get("s");
                if (style == null) {
                    stringBuilder.append((String) m.get("v"));
                } else if (style.equals("bold")) {
                    stringBuilder.append((String) m.get("v"),
                            new StyleSpan(Typeface.BOLD), Spanned.SPAN_MARK_MARK);
                } else if (style.equals("italic")) {
                    stringBuilder.append((String) m.get("v"),
                            new StyleSpan(Typeface.ITALIC), Spanned.SPAN_MARK_MARK);
                } else if (style.equals("highlight")) {
                    stringBuilder.append((String) m.get("v"),
                            new BackgroundColorSpan(context.getResources().getColor(R.color.colorSecondaryLight)), Spanned.SPAN_MARK_MARK);
                }

                stringBuilder.append(" ");
            }
        }

        return stringBuilder;
    }

    private int getHashCode(View view) {
        String type = (String) view.getTag();
        int hash = type.hashCode();

        if (type.equals("text")) {
            hash ^= ((TextView) view).getText().hashCode();

        } else if (type.equals("block")) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
                hash ^= getHashCode(((ViewGroup) view).getChildAt(i));

        } else if (type.equals("header")) {
            hash ^= ((TextView) view).getText().hashCode();

        } else if (type.equals("list")) {
            if (((ViewGroup) view).getChildCount() == 0) return hash;

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
                hash ^= getHashCode(((ViewGroup) view).getChildAt(i));

            String style = (String) ((ViewGroup) view).getChildAt(0).getTag();
            hash ^= style.hashCode();

        } else if (type.equals("rule")) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
                hash ^= getHashCode(((ViewGroup) view).getChildAt(i));
        }

        return hash;
    }

    private int getHashCode(Map<String, Object> node) {
        String type = (String) node.get("t");
        int hash = type.hashCode();

        if (type.equals("text")) {
            hash ^= getStringBuilder(node.get("v")).hashCode();

        } else if (type.equals("block")) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) node.get("v");
            for (int i = 0; i < list.size(); i++)
                hash ^= getHashCode(list.get(i));

        } else if (type.equals("header")) {
            hash ^= getStringBuilder(node.get("v")).hashCode();

        } else if (type.equals("list")) {
            List<Object> list = (List<Object>) node.get("v");

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof String) hash ^= list.get(i).hashCode();
                else hash ^= getHashCode((Map<String, Object>) list.get(i));
            }

            String style = (String) node.get("s");
            hash ^= style.hashCode();

        } else if (type.equals("rule")) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) node.get("v");
            for (int i = 0; i < list.size(); i++)
                hash ^= getHashCode(list.get(i));
        }

        return hash;
    }
}
