package ru.z80sky.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.z80sky.App;
import ru.z80sky.R;
import ru.z80sky.c;
import ru.z80sky.model.MyContentProvider;
public class ExplorerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static float textSize = 0;
    MyContentProvider provider;
    RecyclerView rv;
    public ExplorerAdapter(MyContentProvider cp) {
        provider = cp;
    }
    class DumpViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public DumpViewHolder(@NonNull View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.text);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = view.getTag().hashCode();
                    provider.setCheck(pos, !provider.isChecked(pos));
                    notifyDataSetChanged();
                }
            });
        }
    }
    class CodeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public CodeViewHolder(@NonNull View v) {
            super(v);
        }
    }
   /* @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return 0;//position % 2 * 2;
    }*/
   @Override
   public void onAttachedToRecyclerView(RecyclerView recyclerView) {
       super.onAttachedToRecyclerView(recyclerView);
       rv = recyclerView;
   }
    @Override
    public int getItemCount() {
        return provider.getCnt();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      /*  switch (viewType) {
            case 0:
                return new DumpViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dump, parent, false)
            );
            case 2:
                return new CodeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dump, parent, false)
            );
        }//*/
        return new DumpViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dump, parent, false));
    }
    private float defineSize(TextView tv){
        float sz = 16f;
        String s = App.getInstance().getString(R.string.test_dump);
        int scr_w = App.getInstance().getInt(c.SCR_WIDTH, 1024), width = scr_w;
        //
        do {
            sz-=0.2;
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sz);
            Rect bounds = new Rect();
            Paint textPaint = tv.getPaint();
            textPaint.getTextBounds(s, 0, s.length(), bounds);
            width = bounds.width();
        }while (width>scr_w);
        return sz;
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case 0:
                TextView tv = ((DumpViewHolder) holder).textView;
                if (0==textSize)
                    textSize = defineSize(tv);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                String s = provider.getString(position);
                int begin = provider.getBeginSelect(position);
                if (-1==begin){
                    tv.setText(s);
                }else {
                    int end = provider.getEndSelect(position);
                    Spannable spannable = new SpannableString(s);
                    int pos = position;
                    //spannable.setSpan(new ForegroundColorSpan(Color.BLUE), begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            int new_pos = provider.getLinkTo(pos);//(int) (Math.random() * provider.getCnt());
                            System.out.println("new_pos="+new_pos);
                            rv.scrollToPosition(new_pos);
                        }
                    }, begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(spannable, TextView.BufferType.SPANNABLE);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                }
                tv.setTag(new Integer(position));
                tv.setBackgroundColor(provider.isChecked(position)? c.NEXT_BG : Color.WHITE);
                break;
            case 2:
                CodeViewHolder codeViewHolder = (CodeViewHolder)holder;
                break;
        }
    }
}
