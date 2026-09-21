import React from 'react';
import { IslamicBookCover, IslamicBookCoverProps } from './IslamicBookCover';
import { DarsNizamiBookItem } from '../../types';

export interface BookCoverProps extends Partial<IslamicBookCoverProps> {
  book?: DarsNizamiBookItem;
  size?: 'sm' | 'md' | 'lg' | 'xl';
  className?: string;
  onClick?: () => void;
}

export const BookCover: React.FC<BookCoverProps> = (props) => {
  return <IslamicBookCover {...props} />;
};

export { IslamicBookCover };
export default BookCover;
